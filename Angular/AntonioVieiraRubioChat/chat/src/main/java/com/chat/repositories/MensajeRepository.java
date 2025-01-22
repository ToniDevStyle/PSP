package com.chat.repositories;

import com.chat.models.Mensaje;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MensajeRepository extends MongoRepository<Mensaje, String> {
    List<Mensaje> findByUsername(String username); // Ejemplo: buscar mensajes por usuario

    List<Mensaje> findByChatId(String chatId); // Buscar mensajes por chatId (asegúrate de que tu modelo tiene este campo)
}
