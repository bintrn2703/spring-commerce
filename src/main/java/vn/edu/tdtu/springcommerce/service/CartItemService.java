package vn.edu.tdtu.springcommerce.service;

import vn.edu.tdtu.springcommerce.model.CartItem;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CartItemService {
    Optional<CartItem> getCartItemById(Long id);

    boolean deleteCartItemById(Long id);

    CartItem saveOrUpdateCartItem(CartItem cartItem);

    List<CartItem> getAllCartItems();

    List<CartItem> getCartItemByCartId(Long id);
}
