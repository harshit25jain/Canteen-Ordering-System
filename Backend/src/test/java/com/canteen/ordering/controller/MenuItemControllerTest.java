package com.canteen.ordering.controller;

import com.canteen.ordering.entity.MenuItem;
import com.canteen.ordering.service.MenuItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MenuItemController.class)
class MenuItemControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private MenuItemService menuItemService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
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
    void getAllMenuItems_ShouldReturnAllMenuItems() throws Exception {
        // Given
        List<MenuItem> menuItems = Arrays.asList(menuItem);
        when(menuItemService.getAllMenuItems()).thenReturn(menuItems);
        
        // When & Then
        mockMvc.perform(get("/api/menu"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Burger"))
                .andExpect(jsonPath("$[0].price").value(10.50))
                .andExpect(jsonPath("$[0].stockCount").value(5));
    }
    
    @Test
    void getMenuItemById_WhenExists_ShouldReturnMenuItem() throws Exception {
        // Given
        when(menuItemService.getMenuItemById(1L)).thenReturn(Optional.of(menuItem));
        
        // When & Then
        mockMvc.perform(get("/api/menu/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Burger"))
                .andExpect(jsonPath("$.price").value(10.50))
                .andExpect(jsonPath("$.stockCount").value(5));
    }
    
    @Test
    void getMenuItemById_WhenNotExists_ShouldReturnNotFound() throws Exception {
        // Given
        when(menuItemService.getMenuItemById(1L)).thenReturn(Optional.empty());
        
        // When & Then
        mockMvc.perform(get("/api/menu/1"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void createMenuItem_ShouldCreateAndReturnMenuItem() throws Exception {
        // Given
        when(menuItemService.createMenuItem(any(MenuItem.class))).thenReturn(menuItem);
        
        // When & Then
        mockMvc.perform(post("/api/menu")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(menuItem)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Burger"))
                .andExpect(jsonPath("$.price").value(10.50))
                .andExpect(jsonPath("$.stockCount").value(5));
    }
    
    @Test
    void updateMenuItem_WhenExists_ShouldUpdateAndReturnMenuItem() throws Exception {
        // Given
        MenuItem updatedMenuItem = new MenuItem();
        updatedMenuItem.setName("Updated Burger");
        updatedMenuItem.setPrice(new BigDecimal("12.00"));
        updatedMenuItem.setStockCount(10);
        
        when(menuItemService.updateMenuItem(1L, any(MenuItem.class))).thenReturn(updatedMenuItem);
        
        // When & Then
        mockMvc.perform(put("/api/menu/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedMenuItem)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Burger"))
                .andExpect(jsonPath("$.price").value(12.00))
                .andExpect(jsonPath("$.stockCount").value(10));
    }
    
    @Test
    void deleteMenuItem_WhenExists_ShouldDeleteMenuItem() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/menu/1"))
                .andExpect(status().isNoContent());
    }
    
    @Test
    void getAvailableMenuItems_ShouldReturnAvailableMenuItems() throws Exception {
        // Given
        List<MenuItem> availableItems = Arrays.asList(menuItem);
        when(menuItemService.getAvailableMenuItems()).thenReturn(availableItems);
        
        // When & Then
        mockMvc.perform(get("/api/menu/available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Burger"))
                .andExpect(jsonPath("$[0].stockCount").value(5));
    }
    
    @Test
    void searchMenuItems_ShouldReturnMatchingMenuItems() throws Exception {
        // Given
        List<MenuItem> searchResults = Arrays.asList(menuItem);
        when(menuItemService.searchMenuItems("Burger")).thenReturn(searchResults);
        
        // When & Then
        mockMvc.perform(get("/api/menu/search")
                .param("name", "Burger"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Burger"));
    }
}

