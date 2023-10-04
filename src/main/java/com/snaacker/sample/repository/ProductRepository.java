package com.snaacker.sample.repository;

import com.snaacker.sample.persistent.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(
            value =
                    "SELECT p.* FROM product p LEFT JOIN offer o ON p.offer_id = o.id WHERE"
                            + " o.feed_id = :feedId",
            nativeQuery = true)
    List<Product> getProductByFeedId(Long feedId);
}
