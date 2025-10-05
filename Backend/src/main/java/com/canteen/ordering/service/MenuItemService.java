package com.canteen.ordering.service;

import com.canteen.ordering.entity.MenuItem;
import com.canteen.ordering.repository.MenuItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MenuItemService {
    
    private static final Logger logger = LoggerFactory.getLogger(MenuItemService.class);
    
    @Autowired
    private MenuItemRepository menuItemRepository;
    
    public List<MenuItem> getAllMenuItems() {
        logger.info("Fetching all menu items");
        return menuItemRepository.findAll();
    }
    
    public Optional<MenuItem> getMenuItemById(Long id) {
        logger.info("Fetching menu item with id: {}", id);
        return menuItemRepository.findById(id);
    }
    
    public MenuItem createMenuItem(MenuItem menuItem) {
        logger.info("Creating new menu item: {}", menuItem.getName());
        return menuItemRepository.save(menuItem);
    }
    
    public MenuItem updateMenuItem(Long id, MenuItem menuItemDetails) {
        logger.info("Updating menu item with id: {}", id);
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu item not found with id: " + id));
        
        menuItem.setName(menuItemDetails.getName());
        menuItem.setPrice(menuItemDetails.getPrice());
        menuItem.setStockCount(menuItemDetails.getStockCount());
        
        return menuItemRepository.save(menuItem);
    }
    
    public void deleteMenuItem(Long id) {
        logger.info("Deleting menu item with id: {}", id);
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu item not found with id: " + id));
        
        menuItemRepository.delete(menuItem);
    }
    
    public List<MenuItem> getAvailableMenuItems() {
        logger.info("Fetching available menu items (stock > 0)");
        return menuItemRepository.findByStockCountGreaterThan(0);
    }
    
    public List<MenuItem> searchMenuItems(String name) {
        logger.info("Searching menu items with name containing: {}", name);
        return menuItemRepository.findByNameContainingIgnoreCase(name);
    }
    
    @Transactional
    public boolean decrementStock(Long menuItemId, int quantity) {
        logger.info("Decrementing stock for menu item id: {} by quantity: {}", menuItemId, quantity);
        Optional<MenuItem> menuItemOpt = menuItemRepository.findByIdWithLock(menuItemId);
        
        if (menuItemOpt.isPresent()) {
            MenuItem menuItem = menuItemOpt.get();
            if (menuItem.getStockCount() >= quantity) {
                menuItem.setStockCount(menuItem.getStockCount() - quantity);
                menuItemRepository.save(menuItem);
                logger.info("Successfully decremented stock for menu item: {}", menuItem.getName());
                return true;
            } else {
                logger.warn("Insufficient stock for menu item: {}. Available: {}, Requested: {}", 
                           menuItem.getName(), menuItem.getStockCount(), quantity);
                return false;
            }
        } else {
            logger.error("Menu item not found with id: {}", menuItemId);
            return false;
        }
    }
    
    @Transactional
    public void restoreStock(Long menuItemId, int quantity) {
        logger.info("Restoring stock for menu item id: {} by quantity: {}", menuItemId, quantity);
        Optional<MenuItem> menuItemOpt = menuItemRepository.findByIdWithLock(menuItemId);
        
        if (menuItemOpt.isPresent()) {
            MenuItem menuItem = menuItemOpt.get();
            menuItem.setStockCount(menuItem.getStockCount() + quantity);
            menuItemRepository.save(menuItem);
            logger.info("Successfully restored stock for menu item: {}", menuItem.getName());
        } else {
            logger.error("Menu item not found with id: {}", menuItemId);
        }
    }
}

