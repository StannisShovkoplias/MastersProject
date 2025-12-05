package com.sigmadevs.tech.app.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
@Component
public class ChatHandler implements WebSocketHandler {
    private final Set<WebSocketSession> sessions = new CopyOnWriteArraySet<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
        log.info("Connected");
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        System.out.println(session.getAttributes().get("user"));
        log.info("Received: {}", message.getPayload());
        for (WebSocketSession session1 : sessions) {
            if(session1.isOpen()){
                session1.sendMessage(message);
            }
        }

    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        try {
            session.close();
            sessions.remove(session);
        }catch (Exception e){
            log.error(e.getMessage());
        }
        log.error("Transport error", exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        session.close();
        sessions.remove(session);
        log.info("Connection closed");
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
