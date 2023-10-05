package com.snaacker.sample.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.snaacker.sample.FixtureTest;
import com.snaacker.sample.exception.XMLParserException;
import com.snaacker.sample.exception.XMLParserNotFoundException;
import com.snaacker.sample.exception.XMLParserServerException;
import com.snaacker.sample.model.ProductResponse;
import com.snaacker.sample.persistent.Product;
import com.snaacker.sample.repository.OfferRepository;
import com.snaacker.sample.repository.PriceRepository;
import com.snaacker.sample.repository.ProductRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

public class ProductServiceTest extends FixtureTest {
    private ProductRepository productRepository;
    OfferRepository offerRepository;
    PriceRepository priceRepository;
    private FileProcessService fileProcessService;
    private ProductService productService;

    @Captor private ArgumentCaptor<Product> productArgumentCaptor;

    @BeforeEach
    public void setup() {
        productRepository = mock(ProductRepository.class);
        offerRepository = mock(OfferRepository.class);
        priceRepository = mock(PriceRepository.class);
        fileProcessService = mock(FileProcessService.class);
        productService =
                new ProductService(
                        productRepository, offerRepository, priceRepository, fileProcessService);
    }

    @Test
    public void testLoadProductShouldThrowExceptionWhenSaveFile() {
        doThrow(XMLParserServerException.class)
                .when(fileProcessService)
                .saveFileToServer(anyString(), any());
        MockMultipartFile file =
                new MockMultipartFile(
                        "file", "test.xml", MediaType.TEXT_XML_VALUE, "<Hello><World>".getBytes());
        assertThatThrownBy(() -> productService.loadProducts2DB(file))
                .isInstanceOf(XMLParserServerException.class);
    }

    @Test
    public void testLoadProductShouldThrowExceptionWhenValidateSchema() {
        doThrow(XMLParserServerException.class)
                .when(fileProcessService)
                .schemaValidate(anyString());
        MockMultipartFile file =
                new MockMultipartFile(
                        "file", "test.xml", MediaType.TEXT_XML_VALUE, "<Hello><World>".getBytes());
        when(fileProcessService.saveFileToServer(anyString(), any())).thenReturn("");
        assertThatThrownBy(() -> productService.loadProducts2DB(file))
                .isInstanceOf(XMLParserServerException.class);

        doThrow(XMLParserException.class).when(fileProcessService).schemaValidate(anyString());
        assertThatThrownBy(() -> productService.loadProducts2DB(file))
                .isInstanceOf(XMLParserException.class);
    }

    @Test
    public void testLoadProductShouldThrowExceptionWhenReadObjectFromFile() {
        doThrow(XMLParserServerException.class)
                .when(fileProcessService)
                .readObjectFromFile(anyString());
        MockMultipartFile file =
                new MockMultipartFile(
                        "file", "test.xml", MediaType.TEXT_XML_VALUE, "<Hello><World>".getBytes());
        when(fileProcessService.saveFileToServer(anyString(), any())).thenReturn("");
        doNothing().when(fileProcessService).schemaValidate(anyString());
        assertThatThrownBy(() -> productService.loadProducts2DB(file))
                .isInstanceOf(XMLParserServerException.class);
    }

    @Test
    public void testGetProductShouldSuccess() {
        Product testProduct = new Product();
        testProduct.setName("Test product");
        List<Product> listTestProduct = List.of(testProduct);
        when(productRepository.getProductByFeedId(anyLong())).thenReturn(listTestProduct);
        List<ProductResponse> listProductResponse = productService.getProductsByProductFeedId(1l);
        assertThat(listProductResponse.size()).isEqualTo(1);
    }

    @Test
    public void testGetProductShouldThrowException() {
        when(productRepository.getProductByFeedId(anyLong())).thenReturn(new ArrayList<>());
        assertThatThrownBy(() -> productService.getProductsByProductFeedId(1l))
                .isInstanceOf(XMLParserNotFoundException.class)
                .hasMessageContaining("Object not found");
    }
}
