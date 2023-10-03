package com.snaacker.sample.persistent;

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
    protected long feedId;

    @Column(name = "product_url")
    protected String productUrl;

    @Column(name = "program_name")
    protected String programName;

    @Column(name = "program_logo")
    protected String programLogo;

    @OneToOne
    @JoinColumn(name = "id", referencedColumnName = "price_id")
    protected Price priceHistory;

    @Column(name = "warranty")
    protected String warranty;

    @Column(name = "in_stock")
    protected int inStock;

    @Column(name = "availability")
    protected String availability;

    @Column(name = "delivery_time")
    protected String deliveryTime;

    @Column(name = "condition_")
    protected String condition;

    @Column(name = "shipping_cost")
    protected String shippingCost;

    // id in xml file
    @Column(name = "offer_id")
    protected String offerId;

    @Column(name = "source_product_id")
    protected String sourceProductId;

    @Column(name = "modified_date")
    protected BigInteger modifiedDate;

    @Column(name = "dateFormat")
    protected String dateFormat;
}
