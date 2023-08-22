package io.codejava.OrderService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.codejava.OrderService.entities.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>{

}
