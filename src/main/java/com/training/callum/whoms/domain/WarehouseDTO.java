package com.training.callum.whoms.domain;

/**
 * Data transfer object for warehouse information loaded from external JSON files.
 * Used for reading warehouse data from input/warehouses.json.
 */
public record WarehouseDTO(
    String name,
    String address,
    String city,
    String state,
    String postalCode,
    String warehousePhone,
    int squareFootage,
    int loadingDocks
) {}