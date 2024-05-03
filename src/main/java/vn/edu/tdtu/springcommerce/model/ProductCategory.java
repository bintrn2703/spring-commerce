package vn.edu.tdtu.springcommerce.model;

import lombok.Data;

import jakarta.persistence.*;

@Entity
@Table(name = "product_category")
@Data
public class ProductCategory {

    @EmbeddedId
    private ProductCategoryId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("categoryId")
    @JoinColumn(name = "category_id")
    private Category category;

    // constructors, getters, setters, etc.
}

