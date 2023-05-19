package com.example.pens.config;

import com.example.pens.websocket.DrawingSocketHandler;
import com.example.pens.websocket.SignalingSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(signalingSocketHandler(), "/ws/signal");
        registry.addHandler(drawingSocketHandler(), "/ws/draw");
    }

    @Bean
    public WebSocketHandler signalingSocketHandler() {
        return new SignalingSocketHandler();
    }

    @Bean
    public WebSocketHandler drawingSocketHandler() { return new DrawingSocketHandler(); }

    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxBinaryMessageBufferSize(1024 * 1024); // 1 million bytes
        return container;
    }
}
