package com.training.callum.whoms.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for creating shared beans.
 */
@Configuration
@EnableConfigurationProperties(InputProperties.class)
public class BeanConfiguration {

    private static final Logger log = LoggerFactory.getLogger(BeanConfiguration.class);

    /**
     * Creates and configures the ObjectMapper bean for JSON serialization/deserialization.
     *
     * @return configured ObjectMapper instance
     */
    @Bean
    public ObjectMapper createObjectMapper() {
        log.debug("Creating ObjectMapper bean for JSON processing");
        return new ObjectMapper();
    }
}