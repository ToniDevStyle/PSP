package com.chat.controller;

import com.chat.service.AuthService;
import com.chat.utils.JwtUtil;
import com.chat.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth") // Prefijo común para todas las rutas de este controlador
public class AuthController {

    // Inyección automática de AuthService para manejar lógica relacionada con autenticación
    @Autowired
    private AuthService authService;

    // Inyección automática de JwtUtil para generar tokens JWT
    @Autowired
    private JwtUtil jwtUtil;

    // Punto final para iniciar sesión
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        // Verifica si el nombre de usuario y la contraseña fueron proporcionados
        if (user.getUsername() == null || user.getUsername().isEmpty() || user.getPassword() == null || user.getPassword().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username and password must not be empty.");
        }

        try {
            // Intenta iniciar sesión con las credenciales proporcionadas
            User loggedInUser = authService.login(user.getUsername(), user.getPassword());

            if (loggedInUser != null) {
                // Genera un token JWT para el usuario autenticado
                String token = jwtUtil.generateToken(loggedInUser.getUsername());
                return ResponseEntity.ok(token); // Devuelve el token con un estado 200 OK
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials"); // Credenciales inválidas
            }
        } catch (Exception ex) {
            // Maneja excepciones y devuelve un error de servidor
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error: " + ex.getMessage());
        }
    }

    // Punto final para registrar un nuevo usuario
    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody User user) {
        // Verifica si el nombre de usuario y la contraseña fueron proporcionados
        if (user.getUsername() == null || user.getUsername().isEmpty() || user.getPassword() == null || user.getPassword().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username and password must not be empty.");
        }

        try {
            // Verifica si el usuario ya existe
            if (authService.userExists(user.getUsername())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists."); // Usuario ya registrado
            }

            // Registra al nuevo usuario
            User registeredUser = authService.register(user);

            if (registeredUser != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully."); // Usuario registrado con éxito
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User registration failed."); // Fallo en el registro
            }
        } catch (Exception ex) {
            // Maneja excepciones y devuelve un error de servidor
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error: " + ex.getMessage());
        }
    }
}