package org.saavatech.inventorymgtsystem.repositories;

import org.saavatech.inventorymgtsystem.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {

}
