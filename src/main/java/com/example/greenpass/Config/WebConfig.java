package com.example.greenpass.Config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path uploadDir = Paths.get("uploads").toAbsolutePath().normalize();
        String uploadUri = uploadDir.toUri().toString();

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(
                        uploadUri,
                        uploadDir.resolve("announcements").toUri().toString(),
                        uploadDir.resolve("rewards").toUri().toString(),
                        uploadDir.resolve("reports").toUri().toString(),
                        uploadDir.resolve("users").toUri().toString());
    }
}
