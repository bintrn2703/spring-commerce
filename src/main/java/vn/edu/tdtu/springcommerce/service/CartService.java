package vn.edu.tdtu.springcommerce.service;

import vn.edu.tdtu.springcommerce.dto.CartDTO;
import vn.edu.tdtu.springcommerce.model.Cart;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CartService {
    Optional<CartDTO> getCartDTOById(Long id);

    Optional<Cart> getCartById(Long id);

    boolean deleteCartById(Long id);

    Cart saveOrUpdateCart(Cart cart);

    List<CartDTO> getAllCarts();

    List<CartDTO> getCartByUserId(Long userId);
}
