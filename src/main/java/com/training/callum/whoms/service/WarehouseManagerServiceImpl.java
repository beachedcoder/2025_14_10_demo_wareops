package com.training.callum.whoms.service;

import com.training.callum.whoms.domain.Warehouse;
import com.training.callum.whoms.domain.WarehouseDTO;
import com.training.callum.whoms.domain.WarehouseDTOPatterns;
import com.training.callum.whoms.repository.WarehouseJpaRepository;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of WarehouseManagerService for warehouse persistence and lookup operations.
 */
@Service
@Transactional
public class WarehouseManagerServiceImpl implements WarehouseManagerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseManagerServiceImpl.class);

    private final ExtDataValidationService validationService;
    private final WarehouseJpaRepository repository;

    public WarehouseManagerServiceImpl(
            final ExtDataValidationService validationService,
            final WarehouseJpaRepository repository) {
        this.validationService = validationService;
        this.repository = repository;
    }

    @Override
    public Warehouse persistWarehouseDTO(final WarehouseDTO dto) {
        LOGGER.debug("Persisting WarehouseDTO: {}", dto);
        try {
            validationService.validate(dto);
            final Warehouse entity = convertToEntity(dto);
            final Warehouse saved = repository.save(entity);
            LOGGER.info("Successfully persisted warehouse with id: {}", saved.getId());
            return saved;
        } catch (Exception ex) {
            LOGGER.error("Failed to persist WarehouseDTO: {}", dto, ex);
            throw ex;
        }
    }

    @Override
    public <T> List<Warehouse> warehouseLookUp(final T parameter) {
        LOGGER.debug("Looking up warehouses with parameter: {}", parameter);
        if (parameter == null) {
            LOGGER.warn("Null parameter provided for warehouse lookup");
            return Collections.emptyList();
        }

        final String paramStr = parameter.toString();
        try {
            // Determine search type by validating against patterns
            if (WarehouseDTOPatterns.NAME.getPattern().matcher(paramStr).matches()) {
                LOGGER.debug("Searching by name: {}", paramStr);
                return repository.findByName(paramStr);
            } else if (WarehouseDTOPatterns.POSTAL_CODE.getPattern().matcher(paramStr).matches()) {
                LOGGER.debug("Searching by postal code: {}", paramStr);
                return repository.findByPostalCode(paramStr);
            } else if (WarehouseDTOPatterns.WAREHOUSE_PHONE.getPattern().matcher(paramStr).matches()) {
                LOGGER.debug("Searching by phone: {}", paramStr);
                return repository.findByPhone(paramStr);
            } else {
                LOGGER.warn("Parameter does not match any valid pattern: {}", paramStr);
                throw new IllegalArgumentException(
                        "Parameter does not match name, postal code, or phone pattern: " + paramStr);
            }
        } catch (Exception ex) {
            LOGGER.error("Failed to look up warehouses with parameter: {}", parameter, ex);
            throw ex;
        }
    }

    @Override
    public Warehouse addPerspectiveWarehouse(final Warehouse warehouse) {
        LOGGER.debug("Adding new warehouse: {}", warehouse);
        try {
            validateEntity(warehouse);
            final Warehouse saved = repository.save(warehouse);
            LOGGER.info("Successfully added warehouse with id: {}", saved.getId());
            return saved;
        } catch (Exception ex) {
            LOGGER.error("Failed to add warehouse: {}", warehouse, ex);
            throw ex;
        }
    }

    @Override
    public Warehouse updateWarehouseInformation(final Warehouse warehouse) {
        LOGGER.debug("Updating warehouse: {}", warehouse);
        try {
            validateEntity(warehouse);
            final Warehouse updated = repository.save(warehouse);
            LOGGER.info("Successfully updated warehouse with id: {}", updated.getId());
            return updated;
        } catch (Exception ex) {
            LOGGER.error("Failed to update warehouse: {}", warehouse, ex);
            throw ex;
        }
    }

    /**
     * Convert WarehouseDTO to Warehouse entity.
     *
     * @param dto the DTO to convert
     * @return the Warehouse entity
     */
    private Warehouse convertToEntity(final WarehouseDTO dto) {
        final Warehouse entity = new Warehouse(
                dto.name(),
                dto.address(),
                dto.city(),
                dto.state(),
                dto.postalCode(),
                dto.warehousePhone(),
                dto.squareFootage(),
                dto.loadingDocks());
        entity.setId(UUID.randomUUID());
        return entity;
    }

    /**
     * Validate a Warehouse entity by converting to DTO and validating.
     *
     * @param warehouse the entity to validate
     */
    private void validateEntity(final Warehouse warehouse) {
        final WarehouseDTO dto = new WarehouseDTO(
                warehouse.getName(),
                warehouse.getAddress(),
                warehouse.getCity(),
                warehouse.getState(),
                warehouse.getPostalCode(),
                warehouse.getWarehousePhone(),
                warehouse.getSquareFootage(),
                warehouse.getLoadingDocks());
        validationService.validate(dto);
    }
}