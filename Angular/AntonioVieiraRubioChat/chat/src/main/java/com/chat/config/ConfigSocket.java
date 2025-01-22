package com.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

// Indica que esta clase es una clase de configuración de Spring
@Configuration
// Habilita el uso de WebSocket con un mensaje broker basado en STOMP
@EnableWebSocketMessageBroker
public class ConfigSocket implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Configura los puntos de conexión (endpoints) de STOMP para los clientes
        // Aquí se define el endpoint al que los clientes deben conectarse para establecer la comunicación WebSocket

        registry.addEndpoint("/chat-websocket") // Define el endpoint en "/chat-websocket"
                .setAllowedOrigins("http://localhost:4200", "http://127.0.0.1") // Especifica los dominios permitidos para conectarse al WebSocket
                .withSockJS(); // Habilita SockJS como alternativa para navegadores que no soportan WebSocket nativo
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Configura el message broker, que se encargará de enrutar los mensajes entre el servidor y los clientes

        registry.enableSimpleBroker("/chat/");
        // Habilita un broker simple en memoria que enviará mensajes a los clientes conectados
        // Los mensajes enviados a destinos que comiencen con "/chat/" serán gestionados por este broker

        registry.setApplicationDestinationPrefixes("/app");
        // Prefijo para los mensajes enviados desde el cliente al servidor
        // Los mensajes que empiecen con "/app" serán interpretados como mensajes dirigidos al servidor
    }
}
