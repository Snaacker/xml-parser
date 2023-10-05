package com.snaacker.sample.service;

import com.snaacker.sample.exception.XMLParserNotFoundException;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProductService {
    Logger logger = LoggerFactory.getLogger(ProductService.class);

    final ProductRepository productRepository;
    final OfferRepository offerRepository;
    final PriceRepository priceRepository;
    final FileProcessService fileProcessService;

    @Autowired
    public ProductService(
            final ProductRepository productRepository,
            final OfferRepository offerRepository,
            final PriceRepository priceRepository,
            final FileProcessService fileProcessService) {
        this.productRepository = productRepository;
        this.offerRepository = offerRepository;
        this.priceRepository = priceRepository;
        this.fileProcessService = fileProcessService;
    }

    public List<ProductResponse> getProductsByProductFeedId(long productId) {
        List<com.snaacker.sample.persistent.Product> returnProduct =
                productRepository.getProductByFeedId(productId);
        logger.info("Getting object: " + productId);
        List<ProductResponse> returnList = new ArrayList<>();
        returnProduct.forEach(
                product -> {
                    ProductResponse productResponse = new ProductResponse(product);
                    returnList.add(productResponse);
                });
        if (returnList.isEmpty()) {
            throw new XMLParserNotFoundException("Object not found");
        }
        return returnList;
    }

    public String loadProducts2DB(MultipartFile multipartFile) {
        String fileName =
                StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        logger.info("Start reading " + fileName);
        String tempFileName = fileProcessService.saveFileToServer(fileName, multipartFile);
        fileProcessService.schemaValidate(tempFileName);
        Result result = fileProcessService.readObjectFromFile(tempFileName);
        saveToDB(result);
        return "OK";
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
}
