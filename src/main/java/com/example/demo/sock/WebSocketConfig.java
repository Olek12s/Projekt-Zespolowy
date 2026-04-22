package com.example.demo.sock;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.security.Principal;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
//        config.enableSimpleBroker("/topic");   // server send here (subscriptions)
//        config.setApplicationDestinationPrefixes("/app"); // client sends here

        config.enableSimpleBroker("/topic", "/queue");
        config.setApplicationDestinationPrefixes("/app");
        config.setUserDestinationPrefix("/user");
    }

//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/ws")     // ex: 127.0.0.1:8089/ws
//                .setAllowedOriginPatterns("*")  // any
//                .withSockJS();
//    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .setHandshakeHandler(new org.springframework.web.socket.server.support.DefaultHandshakeHandler() {
                    @Override
                    protected Principal determineUser(
                            org.springframework.http.server.ServerHttpRequest request,
                            org.springframework.web.socket.WebSocketHandler wsHandler,
                            java.util.Map<String, Object> attributes) {

                        return new Principal() {
                            @Override
                            public String getName() {
                                return java.util.UUID.randomUUID().toString();
                            }
                        };
                    }
                })
                .withSockJS();
    }


    // handshake
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {

                StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

                if (StompCommand.CONNECT.equals(accessor.getCommand())) {

                    String sessionId = accessor.getSessionId();

                    accessor.setUser(new Principal() {
                        @Override
                        public String getName() {
                            return sessionId;
                        }
                    });
                }

                return message;
            }
        });
    }
}