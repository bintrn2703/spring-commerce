package vn.edu.tdtu.springcommerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Getter @Setter
    public class Product {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String name;

        @JsonIgnore
        @ManyToMany(fetch = FetchType.LAZY)
        @JoinTable(name = "product_category", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
        private Set<Category> categories = new HashSet<>();

        @JsonIgnore
        @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<CartItem> cartItems = new ArrayList<>();

        private double price;

        private double weight;

        private String description;

        private String imageName;

        private String brand;

        private String color;

    public Product(){
        super();
    }
    public Product(Long productId, String productName, Double price, Double weight, String description, String imageName,String brand, String color) {
        this.id = productId;
        this.name = productName;
        this.price = price;
        this.weight = weight;
        this.description = description;
        this.imageName = imageName;
        this.brand = brand;
        this.color = color;
    }
    public void addCategory(Category category) {
        categories.add(category);
        category.getProducts().add(this);
    }

    public void removeCategory(Category category) {
        categories.remove(category);
        category.getProducts().remove(this);
    }
    @Override
    public String toString(){
        return "id: "+id+", name: "+name+", price: "+price+", image: "+imageName;
    }
}//create table mapping trong db