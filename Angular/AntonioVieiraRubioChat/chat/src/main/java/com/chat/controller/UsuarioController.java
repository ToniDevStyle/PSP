package com.chat.controller;

import com.chat.models.User;
import com.chat.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UserRepository usuarioRepository;

    // Endpoint para buscar un usuario por su nombre de usuario
    @GetMapping("/{username}")
    public User buscarUsuarioPorUsername(@PathVariable String username) {
        Optional<User> usuario = Optional.ofNullable(usuarioRepository.findByUsername(username));
        if (usuario.isPresent()) {
            return usuario.get();
        } else {
            throw new RuntimeException("Usuario no encontrado");
        }
    }
}
