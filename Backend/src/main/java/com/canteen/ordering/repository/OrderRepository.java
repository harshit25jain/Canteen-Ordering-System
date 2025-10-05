package com.canteen.ordering.repository;

import com.canteen.ordering.entity.Order;
import com.canteen.ordering.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    List<Order> findByStatus(OrderStatus status);
    
    List<Order> findByStatusAndCreatedAtBefore(OrderStatus status, LocalDateTime dateTime);
    
    @Query("SELECT o FROM Order o WHERE o.status = :status AND o.createdAt < :dateTime")
    List<Order> findPendingOrdersOlderThan(@Param("status") OrderStatus status, 
                                          @Param("dateTime") LocalDateTime dateTime);
    
    @Query("SELECT o FROM Order o ORDER BY o.createdAt DESC")
    List<Order> findAllOrderByCreatedAtDesc();
}

