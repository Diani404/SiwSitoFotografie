package it.uniroma3.siw.photoblog.configuration;

import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import it.uniroma3.siw.photoblog.model.Cart;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    private final String uploadDir;

    public WebConfiguration(@Value("${app.upload-dir}") String uploadDir) {
        this.uploadDir = uploadDir;
    }

    //foto in locale poi /upload/**
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String location = Paths.get(uploadDir).toAbsolutePath().toUri().toString();
        // la barra finale e' obbligatoria: senza, Spring non la tratta come cartella
        if (!location.endsWith("/")) {
            location = location + "/";
        }
        registry.addResourceHandler("/uploads/**").addResourceLocations(location);
    }

    //un carrello per sessione HTTP dell utente corrente
    @Bean
    @SessionScope
    public Cart cart() {
        return new Cart();
    }
}
