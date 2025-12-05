package com.sigmadevs.tech.security.config;

import com.sigmadevs.tech.app.api.ChatHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistration;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@RequiredArgsConstructor
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final ChatHandler chatHandler;
    private final JwtCookieHandshakeInterceptor jwtCookieHandshakeInterceptor;
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        WebSocketHandlerRegistration registration =
                registry.addHandler(chatHandler, "/chat");
        registration.addInterceptors(jwtCookieHandshakeInterceptor);
        registration.setAllowedOriginPatterns("*");
    }
}
