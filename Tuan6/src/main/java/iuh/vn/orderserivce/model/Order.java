package iuh.vn.orderserivce.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "total_price")
    private Double totalPrice;

    @Column(length = 50)
    private String status; // PENDING, PAID,...

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // 1 Order có nhiều OrderItem
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items;

    // Tự set thời gian khi insert
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}