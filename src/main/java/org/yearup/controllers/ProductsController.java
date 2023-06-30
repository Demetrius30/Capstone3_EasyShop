package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.models.Product;
import org.yearup.data.ProductDao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("products")
@CrossOrigin
public class ProductsController
{
    private ProductDao productDao;

    @Autowired
    public ProductsController(ProductDao productDao)
    {
        this.productDao = productDao;
    }

    @GetMapping("")
//    @PreAuthorize("permitAll()")
    public List<Product> search(@RequestParam(name="cat", required = false) Integer categoryId,
                                @RequestParam(name="minPrice", required = false) BigDecimal minPrice,
                                @RequestParam(name="maxPrice", required = false) BigDecimal maxPrice,
                                @RequestParam(name="color", required = false) String color
                                )
    {
        try
        {
            return productDao.search(categoryId, minPrice, maxPrice, color);
        }
        catch(Exception ex)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    @GetMapping("{id}")
//    @PreAuthorize("permitAll()")
    public Product getById(@PathVariable int id ){
        try
        {
            var product = productDao.getById(id);

            if(product == null)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);

            return product;
        }
        catch(Exception ex)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    @PostMapping()
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Product addProduct(@RequestBody Product product) {
        try
        {
            return productDao.create(product);
        }
        catch(Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    @PutMapping("{id}")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public HashMap<String, String> updateProduct(@PathVariable int id, @RequestBody Product product){
        productDao.update(id, product);

        HashMap<String, String> response = new HashMap<>();

        response.put("Status", "Successful");
        response.put("Message", "Product Updated Successfully");

        return response;

    }

    @DeleteMapping("{id}")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public HashMap<String, String> deleteProduct(@PathVariable int id){
        productDao.delete(id);
        HashMap<String, String> response = new HashMap<>();
        response.put("Status", "Successful");
        response.put("Message", "Product deleted");

        return response;


//        try
//        {
//            var product = productDao.getById(id);
//
//            if(product == null)
//                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
//
//            productDao.delete(id);
//        }
//        catch(Exception ex)
//        {
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
//        }
    }
}
