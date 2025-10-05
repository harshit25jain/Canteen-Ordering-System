package com.canteen.ordering.service;

import com.canteen.ordering.entity.MenuItem;
import com.canteen.ordering.entity.Order;
import com.canteen.ordering.entity.OrderStatus;
import com.canteen.ordering.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderService {
    
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private static final int AUTO_CANCEL_MINUTES = 15;
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private MenuItemService menuItemService;
    
    public List<Order> getAllOrders() {
        logger.info("Fetching all orders");
        return orderRepository.findAllOrderByCreatedAtDesc();
    }
    
    public Optional<Order> getOrderById(Long id) {
        logger.info("Fetching order with id: {}", id);
        return orderRepository.findById(id);
    }
    
    @Transactional
    public Order createOrder(Long menuItemId) {
        logger.info("Creating new order for menu item id: {}", menuItemId);
        
        // Get menu item with lock to prevent race conditions
        Optional<MenuItem> menuItemOpt = menuItemService.getMenuItemById(menuItemId);
        if (!menuItemOpt.isPresent()) {
            throw new RuntimeException("Menu item not found with id: " + menuItemId);
        }
        
        MenuItem menuItem = menuItemOpt.get();
        
        // Check stock availability and decrement
        if (!menuItemService.decrementStock(menuItemId, 1)) {
            throw new RuntimeException("Insufficient stock for menu item: " + menuItem.getName());
        }
        
        // Create order
        Order order = new Order(menuItem);
        Order savedOrder = orderRepository.save(order);
        
        logger.info("Successfully created order with id: {} for menu item: {}", 
                   savedOrder.getId(), menuItem.getName());
        
        return savedOrder;
    }
    
    @Transactional
    public Order cancelOrder(Long orderId) {
        logger.info("Cancelling order with id: {}", orderId);
        
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new RuntimeException("Order is already cancelled");
        }
        
        if (order.getStatus() == OrderStatus.PAID) {
            throw new RuntimeException("Cannot cancel a paid order");
        }
        
        // Restore stock if order was pending
        if (order.getStatus() == OrderStatus.PENDING) {
            menuItemService.restoreStock(order.getMenuItem().getId(), 1);
        }
        
        order.setStatus(OrderStatus.CANCELLED);
        Order savedOrder = orderRepository.save(order);
        
        logger.info("Successfully cancelled order with id: {}", orderId);
        
        return savedOrder;
    }
    
    @Transactional
    public Order payOrder(Long orderId) {
        logger.info("Processing payment for order with id: {}", orderId);
        
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Only pending orders can be paid");
        }
        
        order.setStatus(OrderStatus.PAID);
        Order savedOrder = orderRepository.save(order);
        
        logger.info("Successfully processed payment for order with id: {}", orderId);
        
        return savedOrder;
    }
    
    public List<Order> getOrderHistory() {
        logger.info("Fetching order history");
        return orderRepository.findAllOrderByCreatedAtDesc();
    }
    
    @Scheduled(fixedRate = 60000) // Run every minute
    public void autoCancelPendingOrders() {
        logger.info("Running auto-cancellation check for pending orders");
        
        LocalDateTime cutoffTime = LocalDateTime.now().minusMinutes(AUTO_CANCEL_MINUTES);
        List<Order> pendingOrders = orderRepository.findPendingOrdersOlderThan(OrderStatus.PENDING, cutoffTime);
        
        if (!pendingOrders.isEmpty()) {
            logger.info("Found {} pending orders older than {} minutes", pendingOrders.size(), AUTO_CANCEL_MINUTES);
            
            for (Order order : pendingOrders) {
                try {
                    logger.info("Auto-cancelling order with id: {} created at: {}", 
                               order.getId(), order.getCreatedAt());
                    
                    // Restore stock
                    menuItemService.restoreStock(order.getMenuItem().getId(), 1);
                    
                    // Cancel order
                    order.setStatus(OrderStatus.CANCELLED);
                    orderRepository.save(order);
                    
                    logger.info("Successfully auto-cancelled order with id: {}", order.getId());
                } catch (Exception e) {
                    logger.error("Error auto-cancelling order with id: {}", order.getId(), e);
                }
            }
        } else {
            logger.debug("No pending orders found for auto-cancellation");
        }
    }
    
    public List<Order> getPendingOrders() {
        logger.info("Fetching pending orders");
        return orderRepository.findByStatus(OrderStatus.PENDING);
    }
    
    public List<Order> getPaidOrders() {
        logger.info("Fetching paid orders");
        return orderRepository.findByStatus(OrderStatus.PAID);
    }
    
    public List<Order> getCancelledOrders() {
        logger.info("Fetching cancelled orders");
        return orderRepository.findByStatus(OrderStatus.CANCELLED);
    }
}

