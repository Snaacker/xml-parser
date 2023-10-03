package com.snaacker.sample.model;

import com.snaacker.sample.persistent.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    protected String name;

    protected String description;

    protected short productImageHeight;

    protected short productImageWidth;

    private String productType;

    private String homeDelivery;

    private String company;

    protected String weight;

    protected String size;

    protected String model;

    protected String brand;

    protected String manufacturer;

    protected String techSpecs;

    protected String shortDescription;

    protected String promoText;

    protected String ean;

    protected String upc;

    protected String isbn;

    protected String mpn;

    protected String sku;

    protected String groupingId;

    protected String language;

    public ProductResponse(Product product) {
        this.name = product.getName();

        this.description = product.getDescription();

        this.productImageHeight = product.getProductImageHeight();

        this.productImageWidth = product.getProductImageWidth();

        this.productType = product.getProductType();

        this.homeDelivery = product.getHomeDelivery();

        this.company = product.getCompany();

        this.weight = product.getWeight();

        this.size = product.getSize();

        this.model = product.getModel();

        this.brand = product.getBrand();

        this.manufacturer = product.getManufacturer();

        this.techSpecs = product.getTechSpecs();

        this.shortDescription = product.getShortDescription();

        this.promoText = product.getPromoText();

        this.ean = product.getEan();

        this.upc = product.getUpc();

        this.isbn = product.getIsbn();

        this.mpn = product.getMpn();

        this.sku = product.getSku();

        this.groupingId = product.getGroupingId();

        this.language = product.getLanguage();
    }
}
