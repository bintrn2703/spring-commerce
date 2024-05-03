package vn.edu.tdtu.springcommerce.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Embeddable
public class ProductCategoryId implements Serializable {

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "category_id")
    private Long categoryId;

    public ProductCategoryId() {}

    public ProductCategoryId(Long productId, Long categoryId) {
        this.productId = productId;
        this.categoryId = categoryId;
    }

    // getters, setters, etc.
}