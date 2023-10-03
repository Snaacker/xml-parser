package com.snaacker.sample.persistent;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "category")
public class Category extends BaseObject {
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @Column(name = "name")
    private String name;

    @Column(name = "category_name")
    private String categoryName;
}
