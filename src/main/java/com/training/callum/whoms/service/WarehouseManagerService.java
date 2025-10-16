package com.training.callum.whoms.service;

import com.training.callum.whoms.domain.Warehouse;
import com.training.callum.whoms.domain.WarehouseDTO;

/**
 * Service interface for managing warehouse operations including persistence and lookup.
 */
public interface WarehouseManagerService {

    /**
     * Persist a WarehouseDTO after validation.
     *
     * @param dto the warehouse data transfer object
     * @return the persisted Warehouse entity
     */
    Warehouse persistWarehouseDTO(WarehouseDTO dto);

    /**
     * Look up warehouses by a generic search parameter.
     * The parameter is validated against WarehouseDTOPatterns to determine search type.
     *
     * @param parameter search parameter (name, postal code, or phone)
     * @param <T> type of the parameter
     * @return list of matching Warehouse entities
     */
    <T> java.util.List<Warehouse> warehouseLookUp(T parameter);

    /**
     * Add a new warehouse entity after validation.
     *
     * @param warehouse the warehouse entity to persist
     * @return the persisted Warehouse entity
     */
    Warehouse addPerspectiveWarehouse(Warehouse warehouse);

    /**
     * Update an existing warehouse entity after validation.
     *
     * @param warehouse the warehouse entity to update
     * @return the updated Warehouse entity
     */
    Warehouse updateWarehouseInformation(Warehouse warehouse);
}