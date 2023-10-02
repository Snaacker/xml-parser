package com.snaacker.sample.service;

import com.snaacker.sample.model.ProductResponse;
import com.snaacker.sample.model.xml.output.Result;
import com.snaacker.sample.model.xml.output.Result.Products.Product;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProductService {
    Logger logger = LoggerFactory.getLogger(ProductService.class);

    public List<ProductResponse> getProductsByProductFeedId(long productFeedId) {
        return null;
    }

    public String loadProducts(MultipartFile multipartFile) throws IOException {
        String fileName =
                StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        String uploadFile = saveFile(fileName, multipartFile);
        //    readFile(uploadFile);
        return "Done upload";
    }

    private void readFile(String filename) {
        File xmlFile = new File(filename);

        JAXBContext jaxbContext;
        try {
            jaxbContext = JAXBContext.newInstance(Result.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Result result = (Result) jaxbUnmarshaller.unmarshal(xmlFile);
            for (Product product : result.getProducts().getProduct()) {
                System.out.println(product.getName());
            }
        } catch (JAXBException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    private String saveFile(String fileName, MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get("files-upload");

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileCode = randomAlphanumeric();
        String serverFileName = fileCode + "-" + fileName;
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(serverFileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save file: " + fileName, ioe);
        }

        File xmlFile = new File("files-upload/" + serverFileName);

        JAXBContext jaxbContext;
        try {
            jaxbContext = JAXBContext.newInstance(Result.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Result result = (Result) jaxbUnmarshaller.unmarshal(xmlFile);
            for (Product product : result.getProducts().getProduct()) {
                System.out.println(product.getName());
            }
        } catch (JAXBException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
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
}
