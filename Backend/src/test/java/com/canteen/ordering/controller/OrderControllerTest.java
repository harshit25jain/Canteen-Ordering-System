package com.canteen.ordering.controller;

import com.canteen.ordering.entity.MenuItem;
import com.canteen.ordering.entity.Order;
import com.canteen.ordering.entity.OrderStatus;
import com.canteen.ordering.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private OrderService orderService;
    
    private MenuItem menuItem;
    private Order order;
    
    @BeforeEach
    void setUp() {
        menuItem = new MenuItem();
        menuItem.setId(1L);
        menuItem.setName("Burger");
        menuItem.setPrice(new BigDecimal("10.50"));
        menuItem.setStockCount(5);
        
        order = new Order();
        order.setId(1L);
        order.setMenuItem(menuItem);
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());
    }
    
    @Test
    void getAllOrders_ShouldReturnAllOrders() throws Exception {
        // Given
        List<Order> orders = Arrays.asList(order);
        when(orderService.getAllOrders()).thenReturn(orders);
        
        // When & Then
        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("PENDING"));
    }
    
    @Test
    void getOrderById_WhenExists_ShouldReturnOrder() throws Exception {
        // Given
        when(orderService.getOrderById(1L)).thenReturn(Optional.of(order));
        
        // When & Then
        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }
    
    @Test
    void getOrderById_WhenNotExists_ShouldReturnNotFound() throws Exception {
        // Given
        when(orderService.getOrderById(1L)).thenReturn(Optional.empty());
        
        // When & Then
        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void createOrder_ShouldCreateAndReturnOrder() throws Exception {
        // Given
        when(orderService.createOrder(1L)).thenReturn(order);
        
        // When & Then
        mockMvc.perform(post("/api/orders")
                .param("menuItemId", "1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }
    
    @Test
    void cancelOrder_ShouldCancelAndReturnOrder() throws Exception {
        // Given
        order.setStatus(OrderStatus.CANCELLED);
        when(orderService.cancelOrder(1L)).thenReturn(order);
        
        // When & Then
        mockMvc.perform(post("/api/orders/1/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("CANCELLED"));
    }
    
    @Test
    void payOrder_ShouldPayAndReturnOrder() throws Exception {
        // Given
        order.setStatus(OrderStatus.PAID);
        when(orderService.payOrder(1L)).thenReturn(order);
        
        // When & Then
        mockMvc.perform(post("/api/orders/1/pay"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("PAID"));
    }
    
    @Test
    void getOrderHistory_ShouldReturnOrderHistory() throws Exception {
        // Given
        List<Order> orders = Arrays.asList(order);
        when(orderService.getOrderHistory()).thenReturn(orders);
        
        // When & Then
        mockMvc.perform(get("/api/orders/history"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("PENDING"));
    }
    
    @Test
    void getPendingOrders_ShouldReturnPendingOrders() throws Exception {
        // Given
        List<Order> pendingOrders = Arrays.asList(order);
        when(orderService.getPendingOrders()).thenReturn(pendingOrders);
        
        // When & Then
        mockMvc.perform(get("/api/orders/pending"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("PENDING"));
    }
    
    @Test
    void getPaidOrders_ShouldReturnPaidOrders() throws Exception {
        // Given
        order.setStatus(OrderStatus.PAID);
        List<Order> paidOrders = Arrays.asList(order);
        when(orderService.getPaidOrders()).thenReturn(paidOrders);
        
        // When & Then
        mockMvc.perform(get("/api/orders/paid"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("PAID"));
    }
    
    @Test
    void getCancelledOrders_ShouldReturnCancelledOrders() throws Exception {
        // Given
        order.setStatus(OrderStatus.CANCELLED);
        List<Order> cancelledOrders = Arrays.asList(order);
        when(orderService.getCancelledOrders()).thenReturn(cancelledOrders);
        
        // When & Then
        mockMvc.perform(get("/api/orders/cancelled"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("CANCELLED"));
    }
}

