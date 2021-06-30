package com.example.catalogservice.respository;

import com.example.catalogservice.entity.Catalog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CatalogRepository extends JpaRepository<Catalog, Long> {

    Catalog findByProductId(String productId);
}
