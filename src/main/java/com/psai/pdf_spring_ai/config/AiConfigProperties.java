package com.psai.pdf_spring_ai.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "spring.ai.openai.chat")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Configuration
public class AiConfigProperties {

    private String model;
    private double temperature;
    private String baseUrl;

}
