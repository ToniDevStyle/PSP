package com.chat.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "chats")
public class Chat {
    @Id
    private String id;
    private List<String> participants; // Lista de IDs de usuarios en el chat
    private LocalDateTime createdAt; // Fecha de creación del chat

    // Constructor vacío
    public Chat() {}

    // Constructor completo
    public Chat(List<String> participants, LocalDateTime createdAt) {
        this.participants = participants;
        this.createdAt = createdAt;
    }

    // Getters y setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
