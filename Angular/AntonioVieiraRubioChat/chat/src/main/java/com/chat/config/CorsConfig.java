package com.chat.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    // Logger para registrar información sobre la configuración de CORS
    private static final Logger log = LoggerFactory.getLogger(CorsConfig.class);

    // Declara un bean que define la configuración de CORS
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // Registra un mensaje en el log indicando que se ha configurado CORS
                log.info("CORS configured.");

                registry.addMapping("/**") // Permite CORS para todas las rutas
                        // Especifica los orígenes permitidos para las solicitudes
                        .allowedOrigins("http://localhost:4200", "http://127.0.0.1")
                        // Define los métodos HTTP permitidos
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        // Permite todos los encabezados
                        .allowedHeaders("*")
                        // Permite el uso de credenciales (como cookies o autenticación)
                        .allowCredentials(true);
            }
        };
    }
}