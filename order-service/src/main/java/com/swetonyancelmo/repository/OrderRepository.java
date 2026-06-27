package com.swetonyancelmo.repository;

import com.swetonyancelmo.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
