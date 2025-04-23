package com.backend.attendance.backend.websockets;

import com.backend.attendance.backend.models.StudentSession;
import com.backend.attendance.backend.models.WifiStudentRequest;
import com.backend.attendance.backend.repositories.AccessPointRepository;
import com.backend.attendance.backend.services.WifiAdminService;
import com.backend.attendance.backend.utils.AttendanceProvider;
import com.google.api.client.json.Json;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SocketConnectionHandler extends TextWebSocketHandler {



    @Autowired
    private  AttendanceProvider attendanceProvider;
    @Autowired
    private AccessPointRepository accessPointRepository;


    ConcurrentHashMap<String, StudentSession> studentSessionMap = new ConcurrentHashMap<>();

    private final Gson gson = new Gson();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("Connection established");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session,
                                      CloseStatus status)throws Exception
    {
        System.out.println(session.getId() + " DisConnected");
        for(StudentSession studentSession : studentSessionMap.values()) {
            if (studentSession.getSession().getId().equals(session.getId())) {
                long now = System.currentTimeMillis();
                studentSession.setTotalConnectionTime(studentSession.getTotalConnectionTime() + (now - studentSession.getLastPingTime()));
                studentSession.setLastPingTime(now);
            }
        }
    }
    @Override
    public void handleMessage(WebSocketSession session,
                              WebSocketMessage<?> message)
            throws Exception
    {
        JsonObject obj = gson.fromJson(message.getPayload().toString(), JsonObject.class);
        String email = obj.get("email").getAsString();
        String batch = obj.get("batch").getAsString();
        String year = obj.get("year").getAsString();
        String subject = obj.get("subject").getAsString();
        String bssid = obj.get("bssid").getAsString();

        String attendanceSessionKey = year + ":" + batch + ":" + subject;

        if(!attendanceProvider.getMonitoringStatusMap().containsKey(attendanceSessionKey)){
            session.sendMessage(new TextMessage("Class not started for you yet."));
            session.close(CloseStatus.NOT_ACCEPTABLE);
            return;
        }

        if(!accessPointRepository.checkAccessPoint(bssid)){
            session.sendMessage(new TextMessage("Not connected to campus wifi"));
            session.close(CloseStatus.NOT_ACCEPTABLE);
            return;
        }

        long now = System.currentTimeMillis();

        StudentSession existingSession = studentSessionMap.get(email);
        if (existingSession == null) {
            StudentSession newStudentSession = new StudentSession(email, bssid, now, 0L, session, batch, year, subject);
            newStudentSession.setLastPingTime(now);
            studentSessionMap.put(email, newStudentSession);
        }else{
            Long duration = now - existingSession.getLastPingTime();
            existingSession.setTotalConnectionTime(existingSession.getTotalConnectionTime() + duration);
            existingSession.setLastPingTime(now);
        }
    }

    public ConcurrentHashMap<String, StudentSession> getStudentSessionMap(String year, String batch, String subject) {
        ConcurrentHashMap<String, StudentSession> filteredSessionMap = new ConcurrentHashMap<>();
        for (StudentSession studentSession : studentSessionMap.values()) {
            if (studentSession.getBatch().equals(batch) && studentSession.getSubject().equals(subject) && studentSession.getYear().equals(year)) {
                filteredSessionMap.put(studentSession.getEmail(), studentSession);
            }
        }
        return filteredSessionMap;
    }

    public void stopMonitoring(String year, String batch, String subject) throws IOException {
        studentSessionMap.entrySet().removeIf(entry -> {
            StudentSession s = entry.getValue();
            boolean shouldRemove = s.getBatch().equals(batch)
                    && s.getSubject().equals(subject)
                    && s.getYear().equals(year);
            if (shouldRemove) {
                try {
                    s.getSession().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return shouldRemove;
        });

    }
}
