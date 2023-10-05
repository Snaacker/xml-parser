package com.snaacker.sample.endpoints;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductEndpointIntegrationTest {
    @Autowired private MockMvc mockMvc;

    @Test
    public void testScenario() throws Exception {
        uploadInvalidFileShouldReturnBadRequest();
        uploadValidFileShouldSuccessfully();
        testGetProductWithCorrectFeedIdShouldReturnListProduct();
        testGetNotExistProductShouldReturnNotFound();
    }

    private void testGetNotExistProductShouldReturnNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/product/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Object not found"));
    }

    private void uploadInvalidFileShouldReturnBadRequest() throws Exception {
        Resource fileResource = new ClassPathResource("invalid-xml-1.xml");
        MockMultipartFile firstFile =
                new MockMultipartFile(
                        "test",
                        fileResource.getFilename(),
                        MediaType.MULTIPART_FORM_DATA_VALUE,
                        fileResource.getInputStream());

        assertNotNull(firstFile);
        mockMvc.perform(
                        MockMvcRequestBuilders.multipart("/api/v1/product")
                                .file("file", firstFile.getBytes()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Bad request"));
    }

    private void uploadValidFileShouldSuccessfully() throws Exception {
        Resource fileResource = new ClassPathResource("valid-xml.xml");
        MockMultipartFile firstFile =
                new MockMultipartFile(
                        "sample",
                        fileResource.getFilename(),
                        MediaType.MULTIPART_FORM_DATA_VALUE,
                        fileResource.getInputStream());

        assertNotNull(firstFile);
        mockMvc.perform(
                        MockMvcRequestBuilders.multipart("/api/v1/product")
                                .file("file", firstFile.getBytes()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("OK"));
    }

    private void testGetProductWithCorrectFeedIdShouldReturnListProduct() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/product/99999999"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        MockMvcResultMatchers.content()
                                .json(
                                        "[{\"name\":\"First Test product"
                                            + " name\",\"description\":\"First"
                                            + " description\",\"productImageHeight\":60,\"productImageWidth\":120,\"productType\":\"TV\",\"homeDelivery\":\"Yes\",\"company\":\"Sharp\",\"weight\":\"1999"
                                            + " g\",\"size\":\"110x60x60"
                                            + " cm\",\"model\":\"KajSharpTV000001\",\"brand\":\"Sharp\",\"manufacturer\":\"Sharp\",\"techSpecs\":\"SMART"
                                            + " HUB\",\"shortDescription\":\"Televisions from"
                                            + " Sharp\",\"promoText\":\"Discounted"
                                            + " price!!\",\"ean\":\"ean10100000000000001\",\"upc\":\"upc10100000000000001\",\"isbn\":\"isn10100000000000001\",\"mpn\":\"mpn10100000000000001\",\"sku\":\"SKU1-TV000001\",\"groupingId\":\"skuSKU1-TV000001\",\"language\":\"sv\"}]"));
    }
}
