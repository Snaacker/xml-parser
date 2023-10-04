package com.snaacker.sample.service;

import com.snaacker.sample.exception.XMLParserException;
import com.snaacker.sample.exception.XMLParserServerException;
import com.snaacker.sample.model.xml.output.Result;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Random;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

@Service
public class FileProcessService {

    Logger logger = LoggerFactory.getLogger(FileProcessService.class);
    private static final String SCHEMA_FILE_NAME = "Products_Def.xsd";
    private static final String TEMP_FOLDER_NAME = "files-upload";

    public void schemaValidate(String tempFileName) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            ClassLoader classLoader = getClass().getClassLoader();
            URL resource = classLoader.getResource(SCHEMA_FILE_NAME);
            if (resource == null) {
                logger.error("Schema is missing");
                throw new XMLParserServerException("XSD file is missing");
            }
            Schema schema = factory.newSchema(new File(resource.toURI()));
            Validator validator = schema.newValidator();
            File xmlFile = new File(TEMP_FOLDER_NAME + "/" + tempFileName);
            validator.validate(new StreamSource(xmlFile));
        } catch (IOException | URISyntaxException e) {
            logger.error("Exception while validating file", e);
            throw new XMLParserServerException("Exception while validating file: ", e);
        } catch (SAXException e) {
            logger.error("Schema violate, please provide file with correct schema", e);
            throw new XMLParserException(e);
        }
    }

    public String saveFileToServer(String fileName, MultipartFile multipartFile) {
        Path uploadPath = Paths.get(TEMP_FOLDER_NAME);

        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
            } catch (IOException e) {
                logger.error(e.getMessage());
                throw new XMLParserServerException(e);
            }
        }

        String fileCode = randomAlphanumeric();
        String serverFileName = fileCode + "-" + fileName;
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(serverFileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new XMLParserServerException("Could not save file: " + fileName, e);
        }
        return fileCode + "-" + fileName;
    }

    private String randomAlphanumeric() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt =
                    leftLimit + (int) (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }

    public Result readObjectFromFile(String filename) {
        File xmlFile = new File("files-upload/" + filename);

        JAXBContext jaxbContext;
        try {
            logger.debug("Load object from file " + xmlFile);
            jaxbContext = JAXBContext.newInstance(Result.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            return (Result) jaxbUnmarshaller.unmarshal(xmlFile);
        } catch (JAXBException e) {
            logger.error(e.getMessage());
            throw new XMLParserServerException("Unable to load object", e);
        }
    }
}
