package com.training.callum.whoms.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.callum.whoms.config.InputProperties;
import com.training.callum.whoms.domain.WarehouseDTO;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

/**
 * Service for caching WarehouseDTO objects loaded from external JSON files.
 * Validates and stores warehouse data for efficient access.
 */
@Service
public final class WarehouseDTOCacheService {

    private static final Logger log = LoggerFactory.getLogger(WarehouseDTOCacheService.class);

    private final InputProperties inputProperties;
    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper;
    private final ExtDataValidationService validationService;
    private final List<WarehouseDTO> warehouses = new ArrayList<>();

    public WarehouseDTOCacheService(
            final InputProperties inputProperties,
            final ResourceLoader resourceLoader,
            final ObjectMapper objectMapper,
            final ExtDataValidationService validationService) {
        this.inputProperties = inputProperties;
        this.resourceLoader = resourceLoader;
        this.objectMapper = objectMapper;
        this.validationService = validationService;
    }

    /**
     * Loads warehouse DTOs from the configured file path.
     * Validates each warehouse and caches valid ones.
     */
    @PostConstruct
    public void loadWarehouseDtos() {
        final String warehousePath = inputProperties.file().warehouses();
        log.debug("Loading warehouses from path: {}", warehousePath);

        final Resource resource = resourceLoader.getResource(warehousePath);
        if (!resource.exists()) {
            log.error("Warehouse file not found at path: {}", warehousePath);
            return;
        }

        try (final InputStream inputStream = resource.getInputStream()) {
            final List<WarehouseDTO> loadedWarehouses = objectMapper.readValue(
                    inputStream, new TypeReference<List<WarehouseDTO>>() {});

            for (final WarehouseDTO warehouse : loadedWarehouses) {
                validationService.validate(warehouse);
                warehouses.add(warehouse);
            }

            log.debug("Successfully loaded {} warehouses", warehouses.size());
        } catch (final IOException e) {
            log.error("Failed to load warehouses from path: {}", warehousePath, e);
        }
    }

    /**
     * Returns a copy of the cached warehouse list.
     *
     * @return list of cached WarehouseDTO objects
     */
    public List<WarehouseDTO> getWarehouses() {
        return new ArrayList<>(warehouses);
    }
}