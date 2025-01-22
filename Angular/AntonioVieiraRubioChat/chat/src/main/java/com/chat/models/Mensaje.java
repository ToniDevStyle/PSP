package com.chat.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "mensajes")
public class Mensaje {

    @Id
    private String id;
    private String username;  // Usuario que envía el mensaje
    private String contenido; // Contenido del mensaje
    private String chatId;    // Identificador del chat al que pertenece el mensaje
    private LocalDateTime fechaEnvio; // Fecha de envío

    // Constructor, getters y setters

    public Mensaje() {
    }

    public Mensaje(String username, String contenido, String chatId, LocalDateTime fechaEnvio) {
        this.username = username;
        this.contenido = contenido;
        this.chatId = chatId;
        this.fechaEnvio = fechaEnvio;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(LocalDateTime fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }
}
