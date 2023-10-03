package com.snaacker.sample.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.snaacker.sample.FixtureTest;
import com.snaacker.sample.model.ProductResponse;
import com.snaacker.sample.persistent.Product;
import com.snaacker.sample.repository.OfferRepository;
import com.snaacker.sample.repository.PriceRepository;
import com.snaacker.sample.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ProductServiceTest extends FixtureTest {
    private ProductRepository productRepository;
    OfferRepository offerRepository;
    PriceRepository priceRepository;
    private ProductService productService;

    @BeforeEach
    public void setup() {
        productRepository = mock(ProductRepository.class);
        offerRepository = mock(OfferRepository.class);
        priceRepository = mock(PriceRepository.class);
        productService = new ProductService(productRepository, offerRepository, priceRepository);
    }

    @Test
    public void testProductUpload() {}

    @Test
    public void testGetProduct() {
        Product testProduct = new Product();
        testProduct.setName("Test product");
        when(productRepository.getReferenceById(anyLong())).thenReturn(testProduct);

        ProductResponse returnProduct = productService.getProductsByProductFeedId(1l);
        assertThat(returnProduct.getName()).isEqualTo("Test product");
    }
}
