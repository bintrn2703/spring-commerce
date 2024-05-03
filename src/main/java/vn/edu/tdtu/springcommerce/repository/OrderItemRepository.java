package vn.edu.tdtu.springcommerce.repository;

import vn.edu.tdtu.springcommerce.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Query("SELECT oi.id,oi.order,oi.product,oi.quantity FROM OrderItem oi WHERE oi.order.id = :orderId")
    List<Object[]> findByOrderId(@Param("orderId") Long orderId);
}