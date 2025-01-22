package com.chat.controller;

import com.chat.models.Mensaje;
import com.chat.service.MensajeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class ChatControllers {

    @Autowired
    private MensajeService mensajeService;

    // Manejo de mensajes en tiempo real y guardado en base de datos
    @MessageMapping("/mensaje")  // Esto recibe el mensaje desde el cliente
    @SendTo("/chat/mensaje")  // Esto envía el mensaje a todos los clientes suscritos
    public Mensaje recibeMensaje(Mensaje mensaje) {
        // Configurar atributos adicionales del mensaje
        mensaje.setFechaEnvio(LocalDateTime.now()); // Hora de envío

        // Validar si el usuario no ha enviado un nombre de usuario
        if (mensaje.getUsername() == null || mensaje.getUsername().isEmpty()) {
            mensaje.setUsername("Usuario Anónimo");
        }

        // Validar y asignar un chatId por defecto si no lo tiene
        if (mensaje.getChatId() == null || mensaje.getChatId().isEmpty()) {
            mensaje.setChatId("chat_default");
        }

        // Guardar el mensaje en la base de datos
        Mensaje mensajeGuardado = mensajeService.guardarMensaje(mensaje);

        // Log para depuración
        System.out.println("Mensaje guardado: " + mensajeGuardado);

        // Devolver el mensaje guardado (lo que será enviado a los clientes)
        return mensajeGuardado;
    }
}
