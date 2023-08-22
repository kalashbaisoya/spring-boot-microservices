package io.codejava.ProductService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.codejava.ProductService.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}
