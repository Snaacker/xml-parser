package com.snaacker.sample.service;

import com.snaacker.sample.model.ProductResponse;
import com.snaacker.sample.model.xml.output.Result;
import com.snaacker.sample.model.xml.output.Result.Products.Product;
import com.snaacker.sample.model.xml.output.Result.Products.Product.Offers;
import com.snaacker.sample.persistent.Category;
import com.snaacker.sample.persistent.Offer;
import com.snaacker.sample.persistent.Price;
import com.snaacker.sample.repository.OfferRepository;
import com.snaacker.sample.repository.PriceRepository;
import com.snaacker.sample.repository.ProductRepository;
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
import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

@Service
public class ProductService {
    Logger logger = LoggerFactory.getLogger(ProductService.class);

    final ProductRepository productRepository;
    final OfferRepository offerRepository;
    final PriceRepository priceRepository;

    @Autowired
    public ProductService(
            final ProductRepository productRepository,
            final OfferRepository offerRepository,
            final PriceRepository priceRepository) {
        this.productRepository = productRepository;
        this.offerRepository = offerRepository;
        this.priceRepository = priceRepository;
    }

    public ProductResponse getProductsByProductFeedId(long productId) {
        com.snaacker.sample.persistent.Product returnProduct =
                productRepository.getReferenceById(productId);
        logger.info("Getting object: " + productId);
        return new ProductResponse(returnProduct);
    }

    public String loadProducts(MultipartFile multipartFile) throws IOException {
        String fileName =
                StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        String tempFileName = saveFileToServer(fileName, multipartFile);
        schemaValidate(tempFileName);
        Result result = readObjectFromFile(tempFileName);
        saveToDB(result);
        return "OK";
    }

    private void schemaValidate(String tempFileName) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            ClassLoader classLoader = getClass().getClassLoader();
            URL resource = classLoader.getResource("Products_Def.xsd");
            if (resource == null) {
                throw new IllegalArgumentException("XSD file is missing ");
            }
            Schema schema = factory.newSchema(new File(resource.toURI()));
            Validator validator = schema.newValidator();
            File xmlFile = new File("files-upload/" + tempFileName);
            validator.validate(new StreamSource(xmlFile));
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException("Exception while validating file: " + e.getMessage());
        } catch (SAXException e) {
            throw new RuntimeException(
                    "Schema violate, please provide file with correct schema" + e.getMessage());
        }
    }

    private void saveToDB(Result result) {
        for (Product inputProduct : result.getProducts().getProduct()) {
            com.snaacker.sample.persistent.Product outputProduct =
                    new com.snaacker.sample.persistent.Product();
            outputProduct.setGroupingId(inputProduct.getGroupingId());
            outputProduct.setLanguage(inputProduct.getLanguage());
            outputProduct.setName(inputProduct.getName());
            outputProduct.setDescription(inputProduct.getDescription());
            outputProduct.setProductImageHeight(inputProduct.getProductImage().getHeight());

            Set<Category> setCategory = new HashSet<>();
            inputProduct
                    .getCategories()
                    .getCategory()
                    .forEach(
                            cat -> {
                                Category category = new Category();
                                category.setName(cat.getName());
                                category.setId(cat.getId());
                                category.setCategoryName(cat.getTdCategoryName());
                                setCategory.add(category);
                            });
            outputProduct.setCategories(setCategory);

            outputProduct.setProductImageWidth(inputProduct.getProductImage().getWidth());
            outputProduct.setCompany(
                    inputProduct.getFields().getField().stream()
                            .filter(field -> field.getName().equals("Company"))
                            .findFirst()
                            .get()
                            .getValue());
            outputProduct.setProductType(
                    inputProduct.getFields().getField().stream()
                            .filter(field -> field.getName().equals("ProductType"))
                            .findFirst()
                            .get()
                            .getValue());
            outputProduct.setHomeDelivery(
                    inputProduct.getFields().getField().stream()
                            .filter(field -> field.getName().equals("HomeDelivery"))
                            .findFirst()
                            .get()
                            .getValue());

            outputProduct.setWeight(inputProduct.getWeight());
            outputProduct.setSize(inputProduct.getSize());
            outputProduct.setModel(inputProduct.getModel());
            outputProduct.setBrand(inputProduct.getBrand());
            outputProduct.setManufacturer(inputProduct.getManufacturer());
            outputProduct.setTechSpecs(inputProduct.getTechSpecs());
            outputProduct.setShortDescription(inputProduct.getShortDescription());
            outputProduct.setPromoText(inputProduct.getPromoText());
            outputProduct.setEan(inputProduct.getEan());
            outputProduct.setUpc(inputProduct.getUpc());
            outputProduct.setIsbn(inputProduct.getIsbn());
            outputProduct.setMpn(inputProduct.getMpn());
            outputProduct.setSku(inputProduct.getSku());

            outputProduct.setOffer(getOfferObject(inputProduct));
            logger.debug("Save product to DB");
            productRepository.save(outputProduct);
        }
    }

    private Offer getOfferObject(Product inputProduct) {
        Offers.Offer inputOfferObject = inputProduct.getOffers().getOffer();

        Price price = new Price();
        price.setValue(inputOfferObject.getPriceHistory().getPrice().getValue());
        price.setCurrency(inputOfferObject.getPriceHistory().getPrice().getCurrency());
        price.setValidDate(inputOfferObject.getPriceHistory().getPrice().getDate());
        price.setDateFormat(inputOfferObject.getPriceHistory().getPrice().getDateFormat());
        priceRepository.save(price);

        Offer offer = new Offer();
        offer.setOfferId(inputOfferObject.getId());
        offer.setSourceProductId(inputOfferObject.getSourceProductId());
        offer.setModifiedDate(inputOfferObject.getModifiedDate());
        offer.setFeedId(inputOfferObject.getFeedId());
        offer.setProductUrl(inputOfferObject.getProductUrl());
        offer.setProgramName(inputOfferObject.getProgramName());
        offer.setProgramLogo(inputOfferObject.getProgramLogo());
        offer.setPriceHistory(price);
        offer.setWarranty(inputOfferObject.getWarranty());
        offer.setInStock(inputOfferObject.getInStock());
        offer.setAvailability(inputOfferObject.getAvailability());
        offer.setDeliveryTime(inputOfferObject.getDeliveryTime());
        offer.setCondition(inputOfferObject.getCondition());
        offer.setShippingCost(inputOfferObject.getShippingCost());
        offerRepository.save(offer);
        return offer;
    }

    private Result readObjectFromFile(String filename) {
        File xmlFile = new File("files-upload/" + filename);

        JAXBContext jaxbContext;
        try {
            logger.debug("Load object from file " + xmlFile);
            jaxbContext = JAXBContext.newInstance(Result.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            return (Result) jaxbUnmarshaller.unmarshal(xmlFile);
        } catch (JAXBException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public String saveFileToServer(String fileName, MultipartFile multipartFile)
            throws IOException {
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
