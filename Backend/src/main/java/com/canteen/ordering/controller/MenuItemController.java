package com.canteen.ordering.controller;

import com.canteen.ordering.entity.MenuItem;
import com.canteen.ordering.service.MenuItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/menu")
@Api(tags = "Menu Item Management")
public class MenuItemController {
    
    private static final Logger logger = LoggerFactory.getLogger(MenuItemController.class);
    
    @Autowired
    private MenuItemService menuItemService;
    
    @GetMapping
    @ApiOperation(value = "Get all menu items", notes = "Retrieves all menu items from the system")
    public ResponseEntity<List<MenuItem>> getAllMenuItems() {
        try {
            List<MenuItem> menuItems = menuItemService.getAllMenuItems();
            return ResponseEntity.ok(menuItems);
        } catch (Exception e) {
            logger.error("Error fetching menu items", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/available")
    @ApiOperation(value = "Get available menu items", notes = "Retrieves menu items with stock > 0")
    public ResponseEntity<List<MenuItem>> getAvailableMenuItems() {
        try {
            List<MenuItem> menuItems = menuItemService.getAvailableMenuItems();
            return ResponseEntity.ok(menuItems);
        } catch (Exception e) {
            logger.error("Error fetching available menu items", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/search")
    @ApiOperation(value = "Search menu items", notes = "Search menu items by name")
    public ResponseEntity<List<MenuItem>> searchMenuItems(
            @ApiParam(value = "Search term for menu item name", required = true)
            @RequestParam String name) {
        try {
            List<MenuItem> menuItems = menuItemService.searchMenuItems(name);
            return ResponseEntity.ok(menuItems);
        } catch (Exception e) {
            logger.error("Error searching menu items", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/{id}")
    @ApiOperation(value = "Get menu item by ID", notes = "Retrieves a specific menu item by its ID")
    public ResponseEntity<MenuItem> getMenuItemById(
            @ApiParam(value = "Menu item ID", required = true)
            @PathVariable Long id) {
        try {
            Optional<MenuItem> menuItem = menuItemService.getMenuItemById(id);
            if (menuItem.isPresent()) {
                return ResponseEntity.ok(menuItem.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error fetching menu item with id: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PostMapping
    @ApiOperation(value = "Create new menu item", notes = "Creates a new menu item in the system")
    public ResponseEntity<MenuItem> createMenuItem(
            @ApiParam(value = "Menu item details", required = true)
            @Valid @RequestBody MenuItem menuItem) {
        try {
            MenuItem createdMenuItem = menuItemService.createMenuItem(menuItem);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdMenuItem);
        } catch (Exception e) {
            logger.error("Error creating menu item", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PutMapping("/{id}")
    @ApiOperation(value = "Update menu item", notes = "Updates an existing menu item")
    public ResponseEntity<MenuItem> updateMenuItem(
            @ApiParam(value = "Menu item ID", required = true)
            @PathVariable Long id,
            @ApiParam(value = "Updated menu item details", required = true)
            @Valid @RequestBody MenuItem menuItemDetails) {
        try {
            MenuItem updatedMenuItem = menuItemService.updateMenuItem(id, menuItemDetails);
            return ResponseEntity.ok(updatedMenuItem);
        } catch (RuntimeException e) {
            logger.error("Error updating menu item with id: {}", id, e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error updating menu item with id: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete menu item", notes = "Deletes a menu item from the system")
    public ResponseEntity<Void> deleteMenuItem(
            @ApiParam(value = "Menu item ID", required = true)
            @PathVariable Long id) {
        try {
            menuItemService.deleteMenuItem(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            logger.error("Error deleting menu item with id: {}", id, e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error deleting menu item with id: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

