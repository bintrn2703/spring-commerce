package vn.edu.tdtu.springcommerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Getter @Setter
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User userOrder;

    @JsonIgnore
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    private double order_total;

//    @Column(name = "order_status", columnDefinition = "VARCHAR(30) DEFAULT 'pending'")
    private String order_status;

    private String addition;

    @Column(name = "create_date")
    @CreationTimestamp
    private LocalDateTime createDate;
}
