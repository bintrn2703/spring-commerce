package vn.edu.tdtu.springcommerce.service;

import vn.edu.tdtu.springcommerce.model.OrderItem;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface OrderItemService  {
    Optional<OrderItem> getOrderItemById(Long id);

    boolean deleteOrderItemById(Long id);

    OrderItem saveOrUpdateOrderItem(OrderItem orderItem);

    List<OrderItem> getAllOrderItems();

    List<OrderItem> getOrderItemByOrderId(Long orderId);
}
