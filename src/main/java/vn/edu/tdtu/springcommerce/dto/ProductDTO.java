package vn.edu.tdtu.springcommerce.dto;

import lombok.Data;

import java.util.Set;


@Data
//@Setter @Getter // them
public class ProductDTO {
    private Long id;

    private String name;

    private Set<CategoryDTO> categories;

    private double price;

    private double weight;

    private String description;

    private String imageName;

    private String brand;

    private String color;

}
