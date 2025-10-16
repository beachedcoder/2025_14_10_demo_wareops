package com.training.callum.whoms.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

/**
 * Configuration properties for input data sources.
 * Mapped to properties with prefix "input".
 */
@ConfigurationProperties(prefix = "input")
public record InputProperties(File file) {
    /**
     * Nested record for file-related input properties.
     */
    public record File(
        String warehouses,
        String employees,
        @DefaultValue("20") int maxCount
    ) {
    }
}