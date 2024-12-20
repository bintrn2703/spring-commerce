package vn.edu.tdtu.springcommerce.service.impl;

import vn.edu.tdtu.springcommerce.dto.CartDTO;
import vn.edu.tdtu.springcommerce.model.Cart;
import vn.edu.tdtu.springcommerce.repository.CartRepository;
import vn.edu.tdtu.springcommerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class CartServiceImpl implements CartService {
    @Autowired
    CartRepository cartRepository;

    @Override
    public Optional<CartDTO> getCartDTOById(Long id) {
        return cartRepository.findById(id).map(cart -> {
            CartDTO cartDTO = new CartDTO();
            cartDTO.setId(cart.getId());
            cartDTO.setUserId(cart.getUser().getId());
            cartDTO.setCartTotal(cart.getCart_total());
            return cartDTO;
        });
    }

    @Override
    public Optional<Cart> getCartById(Long id){
        return cartRepository.findById(id);
    }

    @Override
    public boolean deleteCartById(Long id) {
        Optional<Cart> optionalCart = cartRepository.findById(id);
        if (optionalCart.isPresent()) {
            cartRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Cart saveOrUpdateCart(Cart cart) {
        return cartRepository.save(cart);
    }

    @Override
    public List<CartDTO> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();
        List<CartDTO> cartDTOs = new ArrayList<>();
        for (Cart cart : carts) {
            CartDTO cartDTO = new CartDTO();
            cartDTO.setId(cart.getId());
            cartDTO.setUserId(cart.getUser().getId());
            cartDTO.setCartTotal(cart.getCart_total());
            cartDTOs.add(cartDTO);
        }
        return cartDTOs;
    }

    @Override
    public List<CartDTO> getCartByUserId(Long userId) {
        List<Cart> carts = cartRepository.findByUserId(userId);
        List<CartDTO> cartDTOs = new ArrayList<>();
        for (Cart cart : carts) {
            CartDTO cartDTO = new CartDTO();
            cartDTO.setId(cart.getId());
            cartDTO.setUserId(cart.getUser().getId());
            cartDTO.setCartTotal(cart.getCart_total());
            cartDTOs.add(cartDTO);
        }
        return cartDTOs;
    }
}
