package com.snaacker.sample.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.snaacker.sample.FixtureTest;
import com.snaacker.sample.exception.XMLParserException;
import com.snaacker.sample.exception.XMLParserServerException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

public class FileProcessServiceIntegrationTest extends FixtureTest {

    @Autowired FileProcessService fileProcessService;
    private static final String TEMP_FOLDER_NAME = "files-upload";

    @AfterAll
    public static void teardown() throws IOException {
        Files.delete(Paths.get(TEMP_FOLDER_NAME + "/" + "sample.xml"));
        Files.delete(Paths.get(TEMP_FOLDER_NAME + "/" + "test.xml"));
    }

    @BeforeAll
    public static void setup() {
        copyToTestFolder("test.xml");
        copyToTestFolder("sample.xml");
    }

    @Test
    public void testLoadCorrectXMLFileShouldSuccess() {
        assertDoesNotThrow(() -> fileProcessService.schemaValidate("sample.xml"));
    }

    @Test
    public void testLoadNonExistXMLFileShouldThrowException() {
        assertThatThrownBy(() -> fileProcessService.schemaValidate("no-exist.xml"))
                .isInstanceOf(XMLParserServerException.class)
                .hasMessageContaining("Exception while validating file");
    }

    @Test
    public void testLoadInvalidXMLFileShouldThrowException() {
        assertThatThrownBy(() -> fileProcessService.schemaValidate("test.xml"))
                .isInstanceOf(XMLParserException.class)
                .hasMessageContaining(
                        "XML document structures must start and end within the same entity.");
    }

    @Test
    public void testLoadFileToServerShouldSuccessfully() {
        MockMultipartFile mockFile =
                new MockMultipartFile(
                        "mockFile",
                        "test.xml",
                        MediaType.TEXT_XML_VALUE,
                        "<Hello><World>".getBytes());
        String createdFile = fileProcessService.saveFileToServer("test", mockFile);
        File file = new File(TEMP_FOLDER_NAME + "/" + createdFile);
        assertThat(file.exists()).isTrue();
    }

    @Test
    public void testReadObjectFromFileShouldSuccessfully() {
        assertDoesNotThrow(() -> fileProcessService.readObjectFromFile("sample.xml"));
    }

    @Test
    public void testReadObjectFromInvalidXMLFileShouldThrowException() {
        assertThatThrownBy(() -> fileProcessService.readObjectFromFile("test.xml"))
                .isInstanceOf(XMLParserServerException.class)
                .hasMessageContaining("Unable to load object");
    }

    private static void copyToTestFolder(String name) {
        try (InputStream is =
                FileProcessServiceIntegrationTest.class
                        .getClassLoader()
                        .getResourceAsStream(name)) {
            Files.copy(is, Paths.get(TEMP_FOLDER_NAME + "/" + name));
        } catch (IOException e) {
            throw new RuntimeException("Something is wrong");
        }
    }
}
