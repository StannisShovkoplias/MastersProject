package com.sigmadevs.tech.app.api;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.security.Principal;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Arrays;

@RestController
public class SseHandler {


    @GetMapping(path = "/stream-flux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamFlux(HttpServletRequest request) {
        Arrays.stream(request.getCookies()).forEach(cookie -> System.out.println(cookie.getName()));
        return Flux.interval(Duration.ofSeconds(1))
                .map(sequence -> "Flux - " + LocalTime.now().toString());
    }


//    @GetMapping("/sse")
//    public SseEmitter getSse() {
//        SseEmitter sseEmitter = new SseEmitter();
//        sseEmitter
//    }
}
