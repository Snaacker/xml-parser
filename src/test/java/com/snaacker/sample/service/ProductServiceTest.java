package com.snaacker.sample.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.snaacker.sample.FixtureTest;
import com.snaacker.sample.exception.XMLParserException;
import com.snaacker.sample.exception.XMLParserNotFoundException;
import com.snaacker.sample.exception.XMLParserServerException;
import com.snaacker.sample.model.ProductResponse;
import com.snaacker.sample.model.xml.common.Categories;
import com.snaacker.sample.model.xml.common.Categories.Category;
import com.snaacker.sample.model.xml.common.Fields;
import com.snaacker.sample.model.xml.common.Fields.Field;
import com.snaacker.sample.model.xml.common.ProductImage;
import com.snaacker.sample.model.xml.output.Result;
import com.snaacker.sample.model.xml.output.Result.Products;
import com.snaacker.sample.model.xml.output.Result.Products.Product.Offers;
import com.snaacker.sample.model.xml.output.Result.Products.Product.Offers.Offer.PriceHistory;
import com.snaacker.sample.persistent.Offer;
import com.snaacker.sample.persistent.Price;
import com.snaacker.sample.persistent.Product;
import com.snaacker.sample.repository.OfferRepository;
import com.snaacker.sample.repository.PriceRepository;
import com.snaacker.sample.repository.ProductRepository;
import java.math.BigDecimal;
import java.math.BigInteger;
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
    @Captor private ArgumentCaptor<Offer> offerArgumentCaptor;
    @Captor private ArgumentCaptor<Price> priceArgumentCaptor;

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
    public void testLoadProductShouldSuccessWithvalidFile() {
        MockMultipartFile file =
                new MockMultipartFile(
                        "file", "test.xml", MediaType.TEXT_XML_VALUE, "<Hello><World>".getBytes());
        when(fileProcessService.saveFileToServer(anyString(), any())).thenReturn("test-file");
        doNothing().when(fileProcessService).schemaValidate(anyString());

        Result result = generateResultValue();

        when(fileProcessService.readObjectFromFile(anyString())).thenReturn(result);
        when(productRepository.save(any())).thenReturn(new Product());
        when(priceRepository.save(any())).thenReturn(new Price());
        when(offerRepository.save(any())).thenReturn(new Offer());

        String returnString = productService.loadProducts2DB(file);
        verify(productRepository).save(productArgumentCaptor.capture());
        Product product = productArgumentCaptor.getValue();
        assertThat(product.getName()).isEqualTo("Test name");
        assertThat(product.getDescription()).isEqualTo("Test description");
        assertThat(product.getBrand()).isEqualTo("Electrolux");
        assertThat(product.getEan()).isEqualTo("Test");

        verify(offerRepository).save(offerArgumentCaptor.capture());
        Offer offer = offerArgumentCaptor.getValue();
        assertThat(offer.getAvailability()).isEqualTo("test available");
        assertThat(offer.getFeedId()).isEqualTo(111111);

        verify(priceRepository).save(priceArgumentCaptor.capture());
        Price price = priceArgumentCaptor.getValue();
        assertThat(price.getCurrency()).isEqualTo("sek");
        assertThat(price.getValue()).isEqualTo(BigDecimal.valueOf(100));
        assertThat(price.getDateFormat()).isEqualTo("test format");
        assertThat(price.getValidDate()).isEqualTo(new BigInteger(String.valueOf(100)));

        assertThat(returnString).isEqualTo("OK");
    }

    private static Result generateResultValue() {
        Result result = new Result();
        Products.Product inputProduct = new Products.Product();
        inputProduct.setName("Test name");
        inputProduct.setDescription("Test description");
        inputProduct.setBrand("Electrolux");
        inputProduct.setEan("Test");
        ProductImage productImage = new ProductImage();
        productImage.setValue("https://test.com");
        productImage.setHeight((short) 100);
        productImage.setWidth((short) 100);

        Categories categories = new Categories();
        Category category = new Category();
        category.setId(1);
        category.setName("TV");
        category.setTdCategoryName("TV");
        categories.getCategory().add(category);

        Fields fields = new Fields();
        Field companyField = new Field();
        companyField.setName("Company");
        companyField.setValue("Electrolux");
        fields.getField().add(companyField);

        Field productTypeField = new Field();
        productTypeField.setName("ProductType");
        productTypeField.setValue("TV");
        fields.getField().add(productTypeField);

        Field homeDeliveryField = new Field();
        homeDeliveryField.setName("HomeDelivery");
        homeDeliveryField.setValue("true");
        fields.getField().add(homeDeliveryField);

        Offers.Offer offer = new Offers.Offer();
        Offers offers = new Offers();
        offer.setFeedId(111111);
        offer.setAvailability("test available");

        PriceHistory priceHistory = new PriceHistory();
        com.snaacker.sample.model.xml.common.Price price =
                new com.snaacker.sample.model.xml.common.Price();
        price.setCurrency("sek");
        price.setValue(BigDecimal.valueOf(100));
        price.setDateFormat("test format");
        price.setDate(new BigInteger(String.valueOf(100)));
        priceHistory.setPrice(price);
        offer.setPriceHistory(priceHistory);
        offers.setOffer(offer);

        inputProduct.setFields(fields);
        inputProduct.setCategories(categories);
        inputProduct.setProductImage(productImage);
        inputProduct.setOffers(offers);
        Products products = new Products();
        products.getProduct().add(inputProduct);
        result.setProducts(products);
        return result;
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
    public void testLoadProductShouldThrowExceptionWhenValidateSchemaForInvalidFile() {
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
    public void testLoadProductShouldThrowExceptionWhenReadObjectFromFileIsBroken() {
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
