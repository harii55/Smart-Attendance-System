package com.backend.attendance.backend.websockets;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class SocketConnectionHandler extends TextWebSocketHandler {

    List<WebSocketSession> webSocketSessions = Collections.synchronizedList(new ArrayList<WebSocketSession>());


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {


        System.out.println(session.getId() + " Connected");



        webSocketSessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session,
                                      CloseStatus status)throws Exception
    {

        System.out.println(session.getId()
                + " DisConnected");
        webSocketSessions.remove(session);
    }
    @Override
    public void handleMessage(WebSocketSession session,
                              WebSocketMessage<?> message)
            throws Exception
    {

        super.handleMessage(session, message);



        }
}
