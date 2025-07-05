package org.saavatech.inventorymgtsystem.repositories;
import org.saavatech.inventorymgtsystem.models.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier,Long> {
}
