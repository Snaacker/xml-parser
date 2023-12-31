package com.snaacker.sample.persistent;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
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
@Table(name = "price")
public class Price extends BaseObject {
    @Column(name = "price_value")
    private BigDecimal value;

    @Column(name = "currency")
    private String currency;

    // date field in xml
    @Column(name = "valid_date")
    private BigInteger validDate;

    @Column(name = "date_format")
    private String dateFormat;

    @OneToOne(mappedBy = "priceHistory")
    private Offer offer;
}
