package com.snaacker.sample.repository;

import com.snaacker.sample.persistent.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferRepository extends JpaRepository<Offer, Long> {}
