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
    protected String name;

    @Column(name = "description")
    protected String description;

    @Column(name = "product_image_height")
    protected short productImageHeight;

    @Column(name = "product_image_width")
    protected short productImageWidth;

    @OneToMany(mappedBy = "product", cascade = CascadeType.MERGE)
    protected Set<Category> categories;

    @Column(name = "product_type")
    String productType;

    @Column(name = "home_delivery")
    String homeDelivery;

    @Column(name = "company")
    String company;

    @Column(name = "weight")
    protected String weight;

    @Column(name = "size")
    protected String size;

    @Column(name = "model")
    protected String model;

    @Column(name = "brand")
    protected String brand;

    @Column(name = "manufacturer")
    protected String manufacturer;

    @Column(name = "tech_specs")
    protected String techSpecs;

    @Column(name = "short_description")
    protected String shortDescription;

    @Column(name = "promo_text")
    protected String promoText;

    @Column(name = "ean")
    protected String ean;

    @Column(name = "upc")
    protected String upc;

    @Column(name = "isbn")
    protected String isbn;

    @Column(name = "mpn")
    protected String mpn;

    @Column(name = "sku")
    protected String sku;

    @OneToOne
    @JoinColumn(name = "id", referencedColumnName = "offer_id")
    protected Offer offer;

    @Column(name = "grouping_id")
    protected String groupingId;

    @Column(name = "language")
    protected String language;
}
