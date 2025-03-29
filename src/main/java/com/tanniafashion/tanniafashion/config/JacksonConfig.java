package com.tanniafashion.tanniafashion.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {
    @Bean
    public org.springframework.http.converter.json.MappingJackson2HttpMessageConverter MappingJackson2HttpMessageConverter() {
        return new org.springframework.http.converter.json.MappingJackson2HttpMessageConverter();
    } 
}
