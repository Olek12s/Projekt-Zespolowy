package com.example.demo.sock;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class Echo {

    @MessageMapping("/echo")     // /app/echo
    @SendTo("/topic/echo")       // /topic/echo
    public String echo(@Payload String message) {
        System.out.println("Received: " + message);
        return "Echo: " + message;
    }
}