package com.canteen.ordering.controller;

import com.canteen.ordering.entity.Order;
import com.canteen.ordering.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
@Api(tags = "Order Management")
public class OrderController {
    
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    
    @Autowired
    private OrderService orderService;
    
    @GetMapping
    @ApiOperation(value = "Get all orders", notes = "Retrieves all orders from the system")
    public ResponseEntity<List<Order>> getAllOrders() {
        try {
            List<Order> orders = orderService.getAllOrders();
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            logger.error("Error fetching orders", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/history")
    @ApiOperation(value = "Get order history", notes = "Retrieves order history for all users")
    public ResponseEntity<List<Order>> getOrderHistory() {
        try {
            List<Order> orders = orderService.getOrderHistory();
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            logger.error("Error fetching order history", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/pending")
    @ApiOperation(value = "Get pending orders", notes = "Retrieves all pending orders")
    public ResponseEntity<List<Order>> getPendingOrders() {
        try {
            List<Order> orders = orderService.getPendingOrders();
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            logger.error("Error fetching pending orders", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/paid")
    @ApiOperation(value = "Get paid orders", notes = "Retrieves all paid orders")
    public ResponseEntity<List<Order>> getPaidOrders() {
        try {
            List<Order> orders = orderService.getPaidOrders();
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            logger.error("Error fetching paid orders", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/cancelled")
    @ApiOperation(value = "Get cancelled orders", notes = "Retrieves all cancelled orders")
    public ResponseEntity<List<Order>> getCancelledOrders() {
        try {
            List<Order> orders = orderService.getCancelledOrders();
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            logger.error("Error fetching cancelled orders", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/{id}")
    @ApiOperation(value = "Get order by ID", notes = "Retrieves a specific order by its ID")
    public ResponseEntity<Order> getOrderById(
            @ApiParam(value = "Order ID", required = true)
            @PathVariable Long id) {
        try {
            Optional<Order> order = orderService.getOrderById(id);
            if (order.isPresent()) {
                return ResponseEntity.ok(order.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error fetching order with id: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PostMapping
    @ApiOperation(value = "Create new order", notes = "Creates a new order and decrements stock")
    public ResponseEntity<Order> createOrder(
            @ApiParam(value = "Menu item ID", required = true)
            @RequestParam Long menuItemId) {
        try {
            Order order = orderService.createOrder(menuItemId);
            return ResponseEntity.status(HttpStatus.CREATED).body(order);
        } catch (RuntimeException e) {
            logger.error("Error creating order for menu item id: {}", menuItemId, e);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("Error creating order for menu item id: {}", menuItemId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PostMapping("/{id}/cancel")
    @ApiOperation(value = "Cancel order", notes = "Cancels an order and restores stock if not paid")
    public ResponseEntity<Order> cancelOrder(
            @ApiParam(value = "Order ID", required = true)
            @PathVariable Long id) {
        try {
            Order order = orderService.cancelOrder(id);
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            logger.error("Error cancelling order with id: {}", id, e);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("Error cancelling order with id: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PostMapping("/{id}/pay")
    @ApiOperation(value = "Pay order", notes = "Marks an order as paid")
    public ResponseEntity<Order> payOrder(
            @ApiParam(value = "Order ID", required = true)
            @PathVariable Long id) {
        try {
            Order order = orderService.payOrder(id);
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            logger.error("Error processing payment for order with id: {}", id, e);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("Error processing payment for order with id: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

