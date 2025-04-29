// This is the configuration class for WebSocket
// connections. It enables WebSocket and registers the
// SocketConnectionHandler class as the handler for the
// "/hello" endpoint. It also sets the allowed origins to
// "*" so that other domains can also access the socket.


package com.backend.attendance.backend.websockets;

import com.backend.attendance.backend.websockets.SocketConnectionHandler;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig
        implements WebSocketConfigurer {

    private final SocketConnectionHandler socketConnectionHandler;

    public WebSocketConfig(SocketConnectionHandler socketConnectionHandler) {
        this.socketConnectionHandler = socketConnectionHandler;
    }

    @Override
    public void registerWebSocketHandlers(
            WebSocketHandlerRegistry webSocketHandlerRegistry)
    {

        webSocketHandlerRegistry
                .addHandler(socketConnectionHandler,"/attendance/wifi/ws")
                .setAllowedOrigins("*");
    }
}
