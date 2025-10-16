package com.training.callum.whoms.repository;

import com.training.callum.whoms.domain.Warehouse;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WarehouseJpaRepository extends JpaRepository<Warehouse, UUID> {

    List<Warehouse> findByPostalCode(String postalCode);

    @Query("select w from Warehouse w where w.warehousePhone = :phone")
    List<Warehouse> findByPhone(String phone);

    @Query("select w from Warehouse w where lower(w.name) = lower(:name)")
    List<Warehouse> findByName(String name);
}