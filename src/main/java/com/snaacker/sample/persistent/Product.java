package com.snaacker.sample.persistent;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product")
public class Product extends BaseObject {

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "product_image_height")
    private short productImageHeight;

    @Column(name = "product_image_width")
    private short productImageWidth;

    @OneToMany(mappedBy = "product", cascade = CascadeType.MERGE)
    private Set<Category> categories;

    @Column(name = "product_type")
    String productType;

    @Column(name = "home_delivery")
    String homeDelivery;

    @Column(name = "company")
    String company;

    @Column(name = "weight")
    private String weight;

    @Column(name = "size")
    private String size;

    @Column(name = "model")
    private String model;

    @Column(name = "brand")
    private String brand;

    @Column(name = "manufacturer")
    private String manufacturer;

    @Column(name = "tech_specs")
    private String techSpecs;

    @Column(name = "short_description")
    private String shortDescription;

    @Column(name = "promo_text")
    private String promoText;

    @Column(name = "ean")
    private String ean;

    @Column(name = "upc")
    private String upc;

    @Column(name = "isbn")
    private String isbn;

    @Column(name = "mpn")
    private String mpn;

    @Column(name = "sku")
    private String sku;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "offer_id", referencedColumnName = "id")
    private Offer offer;

    @Column(name = "grouping_id")
    private String groupingId;

    @Column(name = "language")
    private String language;
}
