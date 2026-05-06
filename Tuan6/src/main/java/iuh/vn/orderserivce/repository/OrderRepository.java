package iuh.vn.orderserivce.repository;

import iuh.vn.orderserivce.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
