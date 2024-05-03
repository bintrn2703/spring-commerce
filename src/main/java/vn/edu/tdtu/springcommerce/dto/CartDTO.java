package vn.edu.tdtu.springcommerce.dto;

import lombok.Data;

@Data
public class CartDTO {
    private Long id;
    private Long userId;
    private double cartTotal;

    // constructor, getter và setter
}