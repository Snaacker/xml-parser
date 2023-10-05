package com.snaacker.sample.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.snaacker.sample.FixtureTest;
import com.snaacker.sample.model.xml.output.Result;
import com.snaacker.sample.persistent.Offer;
import com.snaacker.sample.persistent.Price;
import com.snaacker.sample.persistent.Product;
import com.snaacker.sample.repository.OfferRepository;
import com.snaacker.sample.repository.PriceRepository;
import com.snaacker.sample.repository.ProductRepository;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

public class ProductServiceIntegrationTest extends FixtureTest {
    private ProductRepository productRepository;
    OfferRepository offerRepository;
    PriceRepository priceRepository;
    private FileProcessService fileProcessService;
    private ProductService productService;

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
    public void testProductUploadShouldSuccess() throws JAXBException, IOException {
        MockMultipartFile file =
                new MockMultipartFile(
                        "file", "test.xml", MediaType.TEXT_XML_VALUE, "<Hello><World>".getBytes());
        when(fileProcessService.saveFileToServer(anyString(), any())).thenReturn("test-file");
        doNothing().when(fileProcessService).schemaValidate(anyString());

        Result result = loadXMLTestObject(VALID_XML_FILE);
        when(fileProcessService.readObjectFromFile(anyString())).thenReturn(result);
        when(productRepository.save(any())).thenReturn(new Product());
        when(priceRepository.save(any())).thenReturn(new Price());
        when(offerRepository.save(any())).thenReturn(new Offer());

        String returnString = productService.loadProducts2DB(file);
        assertThat(returnString).isEqualTo("OK");
    }

    private Result loadXMLTestObject(String fileName) throws JAXBException, IOException {
        Resource fileResource = new ClassPathResource(fileName);

        File xmlFile = fileResource.getFile();

        JAXBContext jaxbContext;
        jaxbContext = JAXBContext.newInstance(Result.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        return (Result) jaxbUnmarshaller.unmarshal(xmlFile);
    }
}
