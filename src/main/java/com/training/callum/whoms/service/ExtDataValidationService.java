package com.training.callum.whoms.service;

import com.training.callum.whoms.domain.WarehouseDTO;
import com.training.callum.whoms.domain.WarehouseDTOPatterns;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service for validating external data objects, specifically WarehouseDTO.
 * Logs errors for each field that fails validation.
 */
@Service
public class ExtDataValidationService {

    private static final Logger log = LoggerFactory.getLogger(ExtDataValidationService.class);

    /**
     * Validates all fields of the given WarehouseDTO.
     * Logs an error for each field that fails validation, including pattern mismatches and range checks.
     *
     * @param dto the WarehouseDTO to validate
     */
    public void validate(final WarehouseDTO dto) {
        validateField(dto.name(), WarehouseDTOPatterns.NAME, "name");
        validateField(dto.address(), WarehouseDTOPatterns.ADDRESS, "address");
        validateField(dto.city(), WarehouseDTOPatterns.CITY, "city");
        validateField(dto.state(), WarehouseDTOPatterns.STATE, "state");
        validateField(dto.postalCode(), WarehouseDTOPatterns.POSTAL_CODE, "postalCode");
        validateField(dto.warehousePhone(), WarehouseDTOPatterns.WAREHOUSE_PHONE, "warehousePhone");
        validateNumericField(dto.squareFootage(), WarehouseDTOPatterns.SQUARE_FOOTAGE, "squareFootage", 1000, 3000000);
        validateNumericField(dto.loadingDocks(), WarehouseDTOPatterns.LOADING_DOCKS, "loadingDocks", 1, 100);
    }

    private void validateField(final String value, final WarehouseDTOPatterns pattern, final String fieldName) {
        if (value == null || !pattern.getPattern().matcher(value).matches()) {
            log.error("Field {} failed validation: {}", fieldName, pattern.getErrorMessage());
        }
    }

    private void validateNumericField(final int value, final WarehouseDTOPatterns pattern, final String fieldName, final int min, final int max) {
        if (value < min || value > max) {
            log.error("Field {} failed validation: value {} not in range {}-{}", fieldName, value, min, max);
        }
    }
}