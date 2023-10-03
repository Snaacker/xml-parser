package com.snaacker.sample.repository;

import com.snaacker.sample.persistent.Price;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceRepository extends JpaRepository<Price, Long> {}
