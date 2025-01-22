package com.chat.controller;

import com.chat.models.Mensaje;
import com.chat.repositories.ChatRepository;
import com.chat.repositories.MensajeRepository;
import com.chat.service.MensajeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mensajes")
public class MensajeController {

    @Autowired
    private MensajeRepository mensajeRepository;

    @Autowired
    private ChatRepository chatRepository;

    // Endpoint para guardar un mensaje en un chat
    @PostMapping
    public Mensaje guardarMensaje(@RequestBody Mensaje mensaje) {
        if (mensaje.getChatId() == null || mensaje.getChatId().isEmpty()) {
            throw new IllegalArgumentException("El chatId es necesario para guardar el mensaje.");
        }

        // Verificar si el chat existe
        chatRepository.findById(mensaje.getChatId()).orElseThrow(() -> new RuntimeException("Chat no encontrado"));

        return mensajeRepository.save(mensaje);
    }

    // Endpoint para obtener los mensajes de un chat específico
    @GetMapping("/chat/{chatId}")
    public List<Mensaje> obtenerMensajesPorChat(@PathVariable String chatId) {
        return mensajeRepository.findByChatId(chatId);
    }
}
