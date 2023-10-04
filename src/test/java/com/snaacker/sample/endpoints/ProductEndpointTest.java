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
public class ProductEndpointTest {
    @Autowired private MockMvc mockMvc;

    @Test
    public void testScenario() throws Exception {
        uploadInvalidFileShouldReturnBadRequest();
        uploadValidFileShouldSuccessfully();
        testGetProductEndpoint();
    }

    public void uploadInvalidFileShouldReturnBadRequest() throws Exception {
        Resource fileResource = new ClassPathResource("test.xml");
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
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    public void uploadValidFileShouldSuccessfully() throws Exception {
        Resource fileResource = new ClassPathResource("sample.xml");
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

    public void testGetProductEndpoint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/product/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
