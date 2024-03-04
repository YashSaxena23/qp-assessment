package qp.grocery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import qp.grocery.model.ItemRequest;
import qp.grocery.service.GroceryServiceImpl;
import qp.grocery.utility.Logging;

@CrossOrigin
@RestController
@RequestMapping("/grocery/admin")
public class AdminController {

    @Autowired
    GroceryServiceImpl groceryService;

    @PostMapping("/add-item")
    @PreAuthorize("haaRole('ADMIN')")
    public ResponseEntity<String> addItem(@RequestParam String name, @RequestParam Long quantity,
                                          @RequestParam Double price){
        Logging.getPostRequestUrl();
        String response = groceryService.addGroceryItem(name, quantity, price);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/add-items")
    public ResponseEntity<String> addItems(@RequestBody ItemRequest itemRequest) {
        return null;
    }
}
