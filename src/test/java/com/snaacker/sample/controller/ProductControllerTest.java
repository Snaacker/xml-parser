package com.snaacker.sample.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.snaacker.sample.FixtureTest;
import com.snaacker.sample.model.ProductResponse;
import com.snaacker.sample.service.ProductService;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class ProductControllerTest extends FixtureTest {

    private ProductController productController;
    private ProductService productService;

    @BeforeEach
    public void setup() {
        productService = mock(ProductService.class);
        productController = new ProductController(productService);
    }

    @Test
    public void testLoadProductShouldSuccess() throws IOException {
        var productResponse = productController.uploadXML(any());
        when(productService.loadProducts(any())).thenReturn("OK");
        assertThat(productResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testGetProductByProductIdShouldSuccess() {
        ProductResponse returnList = new ProductResponse();
        when(productService.getProductsByProductFeedId(anyLong())).thenReturn(returnList);
        var productResponseList = productController.getProductsByProductFeedId(1l);
        assertThat(productResponseList.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
