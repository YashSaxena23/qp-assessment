package qp.grocery.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qp.grocery.entity.Item;
import qp.grocery.model.ItemRequest;
import qp.grocery.repository.ItemRepository;

import java.util.List;

@Service
public class GroceryServiceImpl implements GroceryService{

    @Autowired
    ItemRepository itemRepository;

    @Override
    public String addGroceryItem(String name, Long quantity, Double price) {
        Item item = new Item();
        item.setName(name);
        item.setPricePerUnit(price);
        item.setUnitsLeft(quantity);
        itemRepository.save(item);

        return "Item added";
    }

    @Override
    public void addGroceryItems(List<ItemRequest> itemList) {

    }

    @Override
    public List<Item> getAllGroceryItems() {
        return null;
    }

    @Override
    public void removeGroceryItem(Long id) {

    }

    @Override
    public Item updateGroceryItem(Long id) {
        return null;
    }

    @Override
    public List<Long> updateInventoryLevels(Long id, Long quantity) {
        return null;
    }
}
