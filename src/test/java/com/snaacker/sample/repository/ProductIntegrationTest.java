package com.snaacker.sample.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.snaacker.sample.persistent.Offer;
import com.snaacker.sample.persistent.Product;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class ProductIntegrationTest {
    @Autowired ProductRepository productRepository;

    @Autowired OfferRepository offerRepository;

    @BeforeEach
    public void testFixture() {
        Product product = new Product();
        product.setName("test product");
        product.setDescription("test description");
        product.setLanguage("sv");

        Offer offer = new Offer();
        offer.setProgramName("test programm name");
        offer.setOfferId("test offer id");
        offer.setInStock(1);
        offer.setFeedId(1);
        offerRepository.save(offer);
        product.setOffer(offer);
        productRepository.save(product);
    }

    @Test
    public void getProductByCorrectFeedIdShouldSuccessfully() {
        List<Product> listProduct = productRepository.getProductByFeedId(1l);
        assertThat(listProduct.size()).isEqualTo(1);
        assertThat(listProduct.get(0).getName()).isEqualTo("test product");
        assertThat(listProduct.get(0).getDescription()).isEqualTo("test description");
        assertThat(listProduct.get(0).getLanguage()).isEqualTo("sv");
    }

    @Test
    public void getProductByWrongFeedIdShouldReturnEmptyList() {
        List<Product> listProduct = productRepository.getProductByFeedId(0l);
        assertThat(listProduct.size()).isEqualTo(0);
    }
}
