package com.snaacker.sample.service;

import com.snaacker.sample.model.ProductResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
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

  public List<ProductResponse> getProductsByProductFeedId(long productFeedId){
    return null;
  }

  public String loadProducts(MultipartFile multipartFile) throws IOException {
    String fileName = StringUtils.cleanPath(
        Objects.requireNonNull(multipartFile.getOriginalFilename()));
    return saveFile(fileName, multipartFile);
  }

  private String saveFile(String fileName, MultipartFile multipartFile) throws IOException {
    Path uploadPath = Paths.get("files-upload");

    if (!Files.exists(uploadPath)) {
      Files.createDirectories(uploadPath);
    }

    String fileCode = randomAlphanumeric();

    try (InputStream inputStream = multipartFile.getInputStream()) {
      Path filePath = uploadPath.resolve(fileCode + "-" + fileName);
      Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException ioe) {
      throw new IOException("Could not save file: " + fileName, ioe);
    }

    return fileCode;
  }

  private String randomAlphanumeric() {
    int leftLimit = 97; // letter 'a'
    int rightLimit = 122; // letter 'z'
    int targetStringLength = 10;
    Random random = new Random();
    StringBuilder buffer = new StringBuilder(targetStringLength);
    for (int i = 0; i < targetStringLength; i++) {
      int randomLimitedInt = leftLimit + (int)
          (random.nextFloat() * (rightLimit - leftLimit + 1));
      buffer.append((char) randomLimitedInt);
    }
    return buffer.toString();
  }
}
