package vn.edu.tdtu.springcommerce.dto;

import lombok.Data;

@Data
public class CartItemDTO {
    private Long id;
    private int quantity;
    private Long cartId;
    private Long productId;
}
