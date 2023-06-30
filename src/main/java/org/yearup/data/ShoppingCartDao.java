package org.yearup.data;

import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import java.util.List;

public interface ShoppingCartDao
{
    ShoppingCart getByUserId(int userId);

    void saveShoppingCart(ShoppingCart shoppingCart);
    void deleteShoppingCart(int cartId);
    List<ShoppingCart> getAllCart();
    List<ShoppingCart> getShoppingCartById(int userId);
    void addCartItem(int cartId, ShoppingCartItem shoppingCartItem);
    ShoppingCart create(ShoppingCart shoppingCart);
    void addProductToCart(int userId, int productId);
    void update(int cartId, ShoppingCart shoppingCart);
    void delete(int cartId);
    // add additional method signatures here
}
