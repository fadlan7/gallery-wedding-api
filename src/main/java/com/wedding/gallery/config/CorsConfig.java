package com.wedding.gallery.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // 👈 Membuka akses untuk SEMUA endpoint API kamu
                        .allowedOrigins("http://localhost:3000") // 👈 Izinkan asal dari Frontend lokal kamu
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 👈 Jangan lupa OPTIONS wajib ada untuk preflight!
                        .allowedHeaders("*") // 👈 Izinkan semua header (termasuk Content-Type untuk upload file)
                        .allowCredentials(true); // 👈 Wajib true kalau FE ngirim session/cookies
            }
        };
    }
}