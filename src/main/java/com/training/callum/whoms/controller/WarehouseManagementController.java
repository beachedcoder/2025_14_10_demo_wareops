package com.training.callum.whoms.controller;

import com.training.callum.whoms.domain.WarehouseDTO;
import com.training.callum.whoms.service.ExtDataValidationService;
import com.training.callum.whoms.service.WarehouseDTOCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST controller for warehouse management APIs.
 *
 * All endpoints live under "/whoms/v2/" and accept/produce JSON only.
 */
@Validated
@RestController
@RequestMapping(
    value = "/whoms/v2/",
    produces = MediaType.APPLICATION_JSON_VALUE
)
public class WarehouseManagementController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseManagementController.class);

    private final WarehouseDTOCacheService cacheService;
    private final ExtDataValidationService validationService;

    public WarehouseManagementController(final WarehouseDTOCacheService cacheService,
                                         final ExtDataValidationService validationService) {
        this.cacheService = cacheService;
        this.validationService = validationService;
    }

    /**
     * Return cached warehouses. Responds 200 with an empty list when nothing is cached.
     *
     * GET request may be sent without a body; class-level consumes requires JSON but GET without body is allowed.
     */
    @GetMapping("/warehouses")
    public ResponseEntity<List<WarehouseDTO>> getWarehouses() {
        List<WarehouseDTO> list = cacheService.getWarehouses();
        if (list == null || list.isEmpty()) {
            LOGGER.debug("No warehouses cached - returning empty list");
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(list);
    }

    /**
     * Validate a single WarehouseDTO payload using external validation service.
     *
     * Returns 200 and a JSON object { "valid": true } when validation passes,
     * or 400 with { "valid": false, "error": "<message>" } when validation fails.
     */
    @PostMapping("/warehouses/validate")
    public ResponseEntity<Map<String, Object>> validateWarehouse(@RequestBody final WarehouseDTO warehouse) {
        Map<String, Object> resp = new HashMap<>();
        try {
            validationService.validate(warehouse);
            resp.put("valid", true);
            return ResponseEntity.ok(resp);
        } catch (Exception ex) {
            LOGGER.error("Validation failed for warehouse: {}", warehouse, ex);
            resp.put("valid", false);
            resp.put("error", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
        }
    }
}