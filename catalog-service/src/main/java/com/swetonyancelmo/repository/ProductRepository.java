package com.swetonyancelmo.repository;

import com.swetonyancelmo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
