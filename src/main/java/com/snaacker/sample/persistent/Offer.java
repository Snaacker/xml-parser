package com.snaacker.sample.persistent;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "offer")
public class Offer extends BaseObject {

    @Column(name = "feed_id")
    private long feedId;

    @Column(name = "product_url")
    private String productUrl;

    @Column(name = "program_name")
    private String programName;

    @Column(name = "program_logo")
    private String programLogo;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "price_id", referencedColumnName = "id")
    private Price priceHistory;

    @Column(name = "warranty")
    private String warranty;

    @Column(name = "in_stock")
    private int inStock;

    @Column(name = "availability")
    private String availability;

    @Column(name = "delivery_time")
    private String deliveryTime;

    @Column(name = "condition_")
    private String condition;

    @Column(name = "shipping_cost")
    private String shippingCost;

    // id in xml file
    @Column(name = "offer_id")
    private String offerId;

    @Column(name = "source_product_id")
    private String sourceProductId;

    @Column(name = "modified_date")
    private BigInteger modifiedDate;

    @Column(name = "dateFormat")
    private String dateFormat;

    @OneToOne(mappedBy = "offer")
    private Product product;
}
