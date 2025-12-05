package com.sigmadevs.tech.app.api;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class ChatController {


    @MessageMapping("/app/chat")
    @SendTo("/topic/chat")
    public String syncChat(@Payload String message) {
        System.out.println(message);
        return message+"loh";
    }
}
