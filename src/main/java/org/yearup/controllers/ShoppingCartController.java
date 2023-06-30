package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.ShoppingCart;
import org.yearup.models.User;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;

// convert this class to a REST controller--Completed
@RestController
@RequestMapping("/cart")
@CrossOrigin
// only logged-in users should have access to these actions
public class ShoppingCartController{
    // a shopping cart requires
    private ShoppingCartDao shoppingCartDao;
    private UserDao userDao;
    private ProductDao productDao;


    @Autowired
    public ShoppingCartController(ShoppingCartDao shoppingCartDao, UserDao userDao, ProductDao productDao){
        this.productDao = productDao;
        this.userDao = userDao;
        this.shoppingCartDao = shoppingCartDao;}

    // each method in this controller requires a Principal object as a parameter
    @GetMapping
    public ShoppingCart getCart(Principal principal) {
        try{
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);

            int userId = user.getId();

            return shoppingCartDao.getByUserId(userId);
        }catch(Exception e)
        {
//            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    @GetMapping("/{userId}")
    public ShoppingCart getCartByUserId(@PathVariable int userId){
        System.out.println("User id: " + userId);
        return shoppingCartDao.getByUserId(userId);
    }

    // add a POST method to add a product to the cart - the url should be
    @PostMapping()
    // https://localhost:8080/cart/products/15 (15 is the productId to be added
    public void addToCart(@PathVariable int productId, Principal principal, Authentication authentication){
        try{
            String userName = authentication.getName();
            User user = userDao.getByUserName(userName);

            int userId = user.getId();

            shoppingCartDao.addProductToCart(userId, productId);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "There was an error!");
        }
    }




    // add a PUT method to update an existing product in the cart - the url should be
    @PutMapping("{id}")
    // https://localhost:8080/cart/products/15 (15 is the productId to be updated)
    // the BODY should be a ShoppingCartItem - quantity is the only value that will be updated
    public HashMap<String,String> updateCart(@PathVariable int id, @RequestBody ShoppingCart shoppingCart, Principal principal){
        String userName = principal.getName();
        User user = userDao.getByUserName(userName);
        int userId = user.getId();

        shoppingCartDao.update(id, shoppingCart);

        HashMap<String, String> response = new HashMap<>();

        response.put("Status", "Successful");
        response.put("Message", "Cart Updated Successfully");

        return response;
    }



    // add a DELETE method to clear all products from the current users cart
    @DeleteMapping("{id}")
    // https://localhost:8080/cart
    public HashMap<String, String> deleteCart(@PathVariable int cartId, Principal principal){
        String userName = principal.getName();
        User user = userDao.getByUserName(userName);
        int userId = user.getId();

        shoppingCartDao.delete(cartId);


        HashMap<String,String> response = new HashMap<>();
        response.put("Status", "Successful");
        response.put("Message", "Cart deleted");

        return response;

    }

}
