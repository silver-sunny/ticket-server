package com.kupstudio.bbarge.config;

import com.kupstudio.bbarge.security.properties.AppProperties;
import com.kupstudio.bbarge.security.properties.CorsProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {

    @Bean
    public CorsProperties corsProperties() {
        return new CorsProperties();
    }

    @Bean
    public AppProperties appProperties() {
        return new AppProperties();
    }

}
