package com.chat.config;

import com.chat.filter.JwtAuthenticationFilter;
import com.chat.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.security.config.Customizer.withDefaults;

// Indica que esta clase es una configuración de Spring
@Configuration
public class SecurityConfig {

    // Logger para registrar información sobre la configuración de seguridad
    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    // Inyección de dependencia del componente JwtUtil para manejar tokens JWT
    private final JwtUtil jwtUtil;

    public SecurityConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    // Bean para configurar el codificador de contraseñas usando BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Bean para configurar el AuthenticationManager, que gestiona la autenticación
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class).build();
    }

    // Configuración de la cadena de seguridad de Spring Security
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll() // Permite acceso sin autenticación a rutas que comienzan con '/auth/**'
                        .anyRequest().authenticated() // Requiere autenticación para cualquier otra ruta
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
                // Añade un filtro personalizado (JwtAuthenticationFilter) antes del filtro de autenticación predeterminado
                .csrf(csrf -> csrf.disable()) // Desactiva la protección CSRF (no recomendado en producción sin razones específicas)
                .cors(withDefaults()); // Habilita CORS con la configuración predeterminada

        log.info("Security configuration applied.");
        return http.build();
    }

    // Configuración global de CORS
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200", "http://127.0.0.1"));
        // Define los orígenes permitidos para las solicitudes
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // Especifica los métodos HTTP permitidos
        configuration.setAllowedHeaders(Arrays.asList("*"));
        // Permite todos los encabezados
        configuration.setAllowCredentials(true);
        // Permite el uso de credenciales (cookies, autenticación)

        log.info("CORS configured globally.");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        // Aplica la configuración de CORS a todas las rutas

        return source;
    }
}
