package com.snaacker.sample.repository;

import com.snaacker.sample.persistent.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {}
