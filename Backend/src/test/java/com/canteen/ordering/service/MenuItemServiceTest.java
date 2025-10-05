package com.canteen.ordering.service;

import com.canteen.ordering.entity.MenuItem;
import com.canteen.ordering.repository.MenuItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuItemServiceTest {
    
    @Mock
    private MenuItemRepository menuItemRepository;
    
    @InjectMocks
    private MenuItemService menuItemService;
    
    private MenuItem menuItem;
    
    @BeforeEach
    void setUp() {
        menuItem = new MenuItem();
        menuItem.setId(1L);
        menuItem.setName("Burger");
        menuItem.setPrice(new BigDecimal("10.50"));
        menuItem.setStockCount(5);
    }
    
    @Test
    void getAllMenuItems_ShouldReturnAllMenuItems() {
        // Given
        List<MenuItem> menuItems = Arrays.asList(menuItem);
        when(menuItemRepository.findAll()).thenReturn(menuItems);
        
        // When
        List<MenuItem> result = menuItemService.getAllMenuItems();
        
        // Then
        assertEquals(1, result.size());
        assertEquals("Burger", result.get(0).getName());
        verify(menuItemRepository).findAll();
    }
    
    @Test
    void getMenuItemById_WhenExists_ShouldReturnMenuItem() {
        // Given
        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(menuItem));
        
        // When
        Optional<MenuItem> result = menuItemService.getMenuItemById(1L);
        
        // Then
        assertTrue(result.isPresent());
        assertEquals("Burger", result.get().getName());
        verify(menuItemRepository).findById(1L);
    }
    
    @Test
    void getMenuItemById_WhenNotExists_ShouldReturnEmpty() {
        // Given
        when(menuItemRepository.findById(1L)).thenReturn(Optional.empty());
        
        // When
        Optional<MenuItem> result = menuItemService.getMenuItemById(1L);
        
        // Then
        assertFalse(result.isPresent());
        verify(menuItemRepository).findById(1L);
    }
    
    @Test
    void createMenuItem_ShouldSaveAndReturnMenuItem() {
        // Given
        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(menuItem);
        
        // When
        MenuItem result = menuItemService.createMenuItem(menuItem);
        
        // Then
        assertEquals("Burger", result.getName());
        verify(menuItemRepository).save(menuItem);
    }
    
    @Test
    void updateMenuItem_WhenExists_ShouldUpdateAndReturnMenuItem() {
        // Given
        MenuItem updatedMenuItem = new MenuItem();
        updatedMenuItem.setName("Updated Burger");
        updatedMenuItem.setPrice(new BigDecimal("12.00"));
        updatedMenuItem.setStockCount(10);
        
        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(menuItem));
        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(updatedMenuItem);
        
        // When
        MenuItem result = menuItemService.updateMenuItem(1L, updatedMenuItem);
        
        // Then
        assertEquals("Updated Burger", result.getName());
        verify(menuItemRepository).findById(1L);
        verify(menuItemRepository).save(any(MenuItem.class));
    }
    
    @Test
    void updateMenuItem_WhenNotExists_ShouldThrowException() {
        // Given
        when(menuItemRepository.findById(1L)).thenReturn(Optional.empty());
        
        // When & Then
        assertThrows(RuntimeException.class, () -> {
            menuItemService.updateMenuItem(1L, menuItem);
        });
        verify(menuItemRepository).findById(1L);
        verify(menuItemRepository, never()).save(any(MenuItem.class));
    }
    
    @Test
    void deleteMenuItem_WhenExists_ShouldDeleteMenuItem() {
        // Given
        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(menuItem));
        
        // When
        menuItemService.deleteMenuItem(1L);
        
        // Then
        verify(menuItemRepository).findById(1L);
        verify(menuItemRepository).delete(menuItem);
    }
    
    @Test
    void getAvailableMenuItems_ShouldReturnMenuItemsWithStock() {
        // Given
        List<MenuItem> availableItems = Arrays.asList(menuItem);
        when(menuItemRepository.findByStockCountGreaterThan(0)).thenReturn(availableItems);
        
        // When
        List<MenuItem> result = menuItemService.getAvailableMenuItems();
        
        // Then
        assertEquals(1, result.size());
        verify(menuItemRepository).findByStockCountGreaterThan(0);
    }
    
    @Test
    void searchMenuItems_ShouldReturnMatchingMenuItems() {
        // Given
        List<MenuItem> searchResults = Arrays.asList(menuItem);
        when(menuItemRepository.findByNameContainingIgnoreCase("Burger")).thenReturn(searchResults);
        
        // When
        List<MenuItem> result = menuItemService.searchMenuItems("Burger");
        
        // Then
        assertEquals(1, result.size());
        verify(menuItemRepository).findByNameContainingIgnoreCase("Burger");
    }
}

