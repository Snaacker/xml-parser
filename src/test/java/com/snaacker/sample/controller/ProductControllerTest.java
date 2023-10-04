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
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

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
        Resource fileResource = new ClassPathResource("test.xml");
        MockMultipartFile testFile =
                new MockMultipartFile(
                        "test",
                        fileResource.getFilename(),
                        MediaType.MULTIPART_FORM_DATA_VALUE,
                        fileResource.getInputStream());
        var productResponse = productController.uploadXML(testFile);

        when(productService.loadProducts(any())).thenReturn("OK");
        assertThat(productResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testGetProductByProductIdShouldSuccess() {
        List<ProductResponse> returnList = List.of(new ProductResponse());
        when(productService.getProductsByProductFeedId(anyLong())).thenReturn(returnList);
        var productResponseList = productController.getProductsByProductFeedId(1l);
        assertThat(productResponseList.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
