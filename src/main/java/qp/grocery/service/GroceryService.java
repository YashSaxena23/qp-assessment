package qp.grocery.service;

import org.springframework.stereotype.Service;
import qp.grocery.entity.Item;
import qp.grocery.model.ItemRequest;

import java.util.List;

public interface GroceryService {

    /**
     * Add single item to grocery system
     * @param name item name
     * @param quantity quantity per unit of the item
     * @param price price per unit of the item
     * @return string response/exception occurred of the method
     */
    public String addGroceryItem(String name, Long quantity, Double price);

    public void addGroceryItems(List<ItemRequest> itemList);

    public List<Item> getAllGroceryItems();

    public void removeGroceryItem(Long id);

    public Item updateGroceryItem(Long id);

    public List<Long> updateInventoryLevels(Long id, Long quantity);
}
