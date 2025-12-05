package com.sigmadevs.tech.security.config;

import com.sigmadevs.tech.app.entity.User;
import com.sigmadevs.tech.security.exception.NotFoundException;
import com.sigmadevs.tech.security.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.security.Principal;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtCookieHandshakeInterceptor extends HttpSessionHandshakeInterceptor{
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
        String jwtToken = null;
        Cookie cookie = Arrays.stream(Optional.ofNullable(servletRequest.getCookies()).orElse(new Cookie[]{}))
                .filter(cookieElement -> "accessToken".equals(cookieElement.getName()))
                .findFirst().orElse(null);
        jwtToken = cookie==null?null:cookie.getValue();
        if (jwtToken == null) {
            log.debug("no accessToken ");

            return false;
        }
        if (!jwtToken.isBlank() && jwtUtil.validateToken(jwtToken)) {
            String username = jwtUtil.getUsername(jwtToken);
            User user;
            try {
                user = userService.findByUsername(username);
            } catch (NotFoundException e) {
                log.debug("Token contains a non-existent user");
//                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token contains a non-existent user");
                return false;
            }
//            if (user != null ) {
                attributes.put("user", user);
                return true;
//            }
        }
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
