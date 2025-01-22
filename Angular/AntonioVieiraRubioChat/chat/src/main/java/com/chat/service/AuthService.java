package com.chat.service;

import com.chat.models.User;
import com.chat.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

@Service
public class AuthService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Registro de usuario
    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // Inicio de sesión
    public User login(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            log.info("User found: {}", user.getUsername());
            if (passwordEncoder.matches(password, user.getPassword())) {
                log.info("Password matched for user: {}", user.getUsername());
                return user;
            } else {
                log.warn("Invalid password for user: {}", user.getUsername());
            }
        } else {
            log.warn("User not found: {}", username);
        }
        return null;
    }

    // Método requerido por UserDetailsService
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            log.warn("User not found: {}", username);
            throw new UsernameNotFoundException("User not found: " + username);
        }

        // Convertir el usuario en un UserDetails
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(Collections.emptyList()) // Agregar roles o permisos si los tienes
                .build();
    }

    public boolean userExists(String username) {
        return userRepository.findByUsername(username) != null;
    }

}
