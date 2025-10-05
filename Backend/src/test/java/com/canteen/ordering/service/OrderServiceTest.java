package com.canteen.ordering.service;

import com.canteen.ordering.entity.MenuItem;
import com.canteen.ordering.entity.Order;
import com.canteen.ordering.entity.OrderStatus;
import com.canteen.ordering.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    
    @Mock
    private OrderRepository orderRepository;
    
    @Mock
    private MenuItemService menuItemService;
    
    @InjectMocks
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
    void getAllOrders_ShouldReturnAllOrders() {
        // Given
        List<Order> orders = Arrays.asList(order);
        when(orderRepository.findAllOrderByCreatedAtDesc()).thenReturn(orders);
        
        // When
        List<Order> result = orderService.getAllOrders();
        
        // Then
        assertEquals(1, result.size());
        verify(orderRepository).findAllOrderByCreatedAtDesc();
    }
    
    @Test
    void getOrderById_WhenExists_ShouldReturnOrder() {
        // Given
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        
        // When
        Optional<Order> result = orderService.getOrderById(1L);
        
        // Then
        assertTrue(result.isPresent());
        assertEquals(OrderStatus.PENDING, result.get().getStatus());
        verify(orderRepository).findById(1L);
    }
    
    @Test
    void createOrder_WhenMenuItemExistsAndStockAvailable_ShouldCreateOrder() {
        // Given
        when(menuItemService.getMenuItemById(1L)).thenReturn(Optional.of(menuItem));
        when(menuItemService.decrementStock(1L, 1)).thenReturn(true);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        
        // When
        Order result = orderService.createOrder(1L);
        
        // Then
        assertNotNull(result);
        verify(menuItemService).getMenuItemById(1L);
        verify(menuItemService).decrementStock(1L, 1);
        verify(orderRepository).save(any(Order.class));
    }
    
    @Test
    void createOrder_WhenMenuItemNotExists_ShouldThrowException() {
        // Given
        when(menuItemService.getMenuItemById(1L)).thenReturn(Optional.empty());
        
        // When & Then
        assertThrows(RuntimeException.class, () -> {
            orderService.createOrder(1L);
        });
        verify(menuItemService).getMenuItemById(1L);
        verify(menuItemService, never()).decrementStock(anyLong(), anyInt());
        verify(orderRepository, never()).save(any(Order.class));
    }
    
    @Test
    void createOrder_WhenInsufficientStock_ShouldThrowException() {
        // Given
        when(menuItemService.getMenuItemById(1L)).thenReturn(Optional.of(menuItem));
        when(menuItemService.decrementStock(1L, 1)).thenReturn(false);
        
        // When & Then
        assertThrows(RuntimeException.class, () -> {
            orderService.createOrder(1L);
        });
        verify(menuItemService).getMenuItemById(1L);
        verify(menuItemService).decrementStock(1L, 1);
        verify(orderRepository, never()).save(any(Order.class));
    }
    
    @Test
    void cancelOrder_WhenOrderExistsAndPending_ShouldCancelOrder() {
        // Given
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        
        // When
        Order result = orderService.cancelOrder(1L);
        
        // Then
        assertEquals(OrderStatus.CANCELLED, result.getStatus());
        verify(menuItemService).restoreStock(1L, 1);
        verify(orderRepository).save(any(Order.class));
    }
    
    @Test
    void cancelOrder_WhenOrderNotExists_ShouldThrowException() {
        // Given
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());
        
        // When & Then
        assertThrows(RuntimeException.class, () -> {
            orderService.cancelOrder(1L);
        });
        verify(orderRepository).findById(1L);
        verify(menuItemService, never()).restoreStock(anyLong(), anyInt());
        verify(orderRepository, never()).save(any(Order.class));
    }
    
    @Test
    void cancelOrder_WhenOrderAlreadyCancelled_ShouldThrowException() {
        // Given
        order.setStatus(OrderStatus.CANCELLED);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        
        // When & Then
        assertThrows(RuntimeException.class, () -> {
            orderService.cancelOrder(1L);
        });
        verify(orderRepository).findById(1L);
        verify(menuItemService, never()).restoreStock(anyLong(), anyInt());
        verify(orderRepository, never()).save(any(Order.class));
    }
    
    @Test
    void payOrder_WhenOrderExistsAndPending_ShouldMarkAsPaid() {
        // Given
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        
        // When
        Order result = orderService.payOrder(1L);
        
        // Then
        assertEquals(OrderStatus.PAID, result.getStatus());
        verify(orderRepository).save(any(Order.class));
    }
    
    @Test
    void payOrder_WhenOrderNotExists_ShouldThrowException() {
        // Given
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());
        
        // When & Then
        assertThrows(RuntimeException.class, () -> {
            orderService.payOrder(1L);
        });
        verify(orderRepository).findById(1L);
        verify(orderRepository, never()).save(any(Order.class));
    }
    
    @Test
    void payOrder_WhenOrderNotPending_ShouldThrowException() {
        // Given
        order.setStatus(OrderStatus.PAID);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        
        // When & Then
        assertThrows(RuntimeException.class, () -> {
            orderService.payOrder(1L);
        });
        verify(orderRepository).findById(1L);
        verify(orderRepository, never()).save(any(Order.class));
    }
    
    @Test
    void getOrderHistory_ShouldReturnAllOrders() {
        // Given
        List<Order> orders = Arrays.asList(order);
        when(orderRepository.findAllOrderByCreatedAtDesc()).thenReturn(orders);
        
        // When
        List<Order> result = orderService.getOrderHistory();
        
        // Then
        assertEquals(1, result.size());
        verify(orderRepository).findAllOrderByCreatedAtDesc();
    }
    
    @Test
    void getPendingOrders_ShouldReturnPendingOrders() {
        // Given
        List<Order> pendingOrders = Arrays.asList(order);
        when(orderRepository.findByStatus(OrderStatus.PENDING)).thenReturn(pendingOrders);
        
        // When
        List<Order> result = orderService.getPendingOrders();
        
        // Then
        assertEquals(1, result.size());
        verify(orderRepository).findByStatus(OrderStatus.PENDING);
    }
}

