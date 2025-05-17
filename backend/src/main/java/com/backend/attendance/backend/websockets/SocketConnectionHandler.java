package com.backend.attendance.backend.websockets;

import com.backend.attendance.backend.models.StudentSession;
import com.backend.attendance.backend.repositories.AccessPointRepository;
import com.backend.attendance.backend.repositories.StudentRepository;
import com.backend.attendance.backend.services.JwtService;
import com.backend.attendance.backend.utils.AttendanceProvider;
import com.backend.attendance.backend.utils.StudentProvider;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SocketConnectionHandler extends TextWebSocketHandler {



    @Autowired
    private  AttendanceProvider attendanceProvider;
    @Autowired
    private AccessPointRepository accessPointRepository;
    @Autowired
    private StudentProvider studentProvider;

    @Autowired
            private JwtService jwtService;

    ConcurrentHashMap<String, StudentSession> studentSessionMap = new ConcurrentHashMap<>();

    private final Gson gson = new Gson();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
       String token = session.getUri().getQuery().replace("token=", "");
         if (token == null || token.isEmpty()) {
              session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Missing token"));
              return;
         }
       if (!jwtService.isValidToken(token)){
           session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Invalid or missing token"));
           return;
       }

       String email = jwtService.getEmailFromToken(token);
       session.getAttributes().put("email", email);
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
                studentSession.setIsConnected(false);
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
        String bssid = obj.get("bssid").getAsString();
        if (!studentProvider.getStudentDirectory().containsKey(email)) {
            session.sendMessage(new TextMessage("Email address does not exist."));
            System.out.println(email + ": wrong email");
            session.close(CloseStatus.NOT_ACCEPTABLE);
            return;
        }

        String batch = studentProvider.getStudentDirectory().get(email).getBatch();
        String year = studentProvider.getStudentDirectory().get(email).getYear();
        String subject = attendanceProvider.getSubjectMap().get(year + ":" + batch);
        String attendanceSessionKey = year + ":" + batch + ":" + subject;

        if(!attendanceProvider.getMonitoringStatusMap().containsKey(attendanceSessionKey)){
            session.sendMessage(new TextMessage("Class not started for you yet."));
            System.out.println(email + ": Class not started for you yet.");
            session.close(CloseStatus.NOT_ACCEPTABLE);
            return;
        }

        if(!accessPointRepository.checkAccessPoint(bssid)){
            session.sendMessage(new TextMessage("Not connected to campus wifi"));
            System.out.println(email + ": Wrong WIFI");
            session.close(CloseStatus.NOT_ACCEPTABLE);
            return;
        }

        long now = System.currentTimeMillis();

        StudentSession existingSession = studentSessionMap.get(email);
        if (existingSession == null) {
            StudentSession newStudentSession = new StudentSession(email, bssid, now, 0L, session, batch, year, subject, true);
            newStudentSession.setLastPingTime(now);
            studentSessionMap.put(email, newStudentSession);
        }else{

            Long duration = now - existingSession.getLastPingTime();
            if (existingSession.getIsConnected()) {
                existingSession.setTotalConnectionTime(existingSession.getTotalConnectionTime() + duration);
                existingSession.setLastPingTime(now);
            }else{
                existingSession.setLastPingTime(now);
                existingSession.setIsConnected(true);
            }
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
