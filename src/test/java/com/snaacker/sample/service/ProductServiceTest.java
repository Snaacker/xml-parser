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
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

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
    public void testProductUploadShouldSuccess() {
        //        MockMultipartFile file =
        //                new MockMultipartFile(
        //                        "file", "test.xml", MediaType.TEXT_XML_VALUE,
        // "<Hello><World>".getBytes());
        //        when(fileProcessService.saveFileToServer(anyString(),
        // any())).thenReturn("test-file");
        //        doNothing().when(fileProcessService).schemaValidate(anyString());
        //        Result result = new Result();
        //        when(fileProcessService.readObjectFromFile(anyString())).thenReturn(result);
        //        when(productRepository.save(any())).thenReturn(new Product());
        //        when(priceRepository.save(any())).thenReturn(new Price());
        //        when(offerRepository.save(any())).thenReturn(new Offer());
        //
        //        String returnString = productService.loadProducts(file);
        //        assertThat(returnString).isEqualTo("OK");
    }

    @Test
    public void testGetProduct() {
        Product testProduct = new Product();
        testProduct.setName("Test product");
        List<Product> listTestProduct = List.of(testProduct);
        when(productRepository.getProductByFeedId(anyLong())).thenReturn(listTestProduct);

        List<ProductResponse> listProductResponse = productService.getProductsByProductFeedId(1l);

        assertThat(listProductResponse.size()).isEqualTo(1);
    }
}
