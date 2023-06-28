package org.yearup.data;

import org.yearup.models.ShoppingCart;

import java.util.List;

public interface ShoppingCartDao
{
    ShoppingCart getByUserId(int userId);

    List<ShoppingCart> getAllCart();
    ShoppingCart create(ShoppingCart shoppingCart);
    void update(int cartId, ShoppingCart shoppingCart);
    void delete(int cartId);
    // add additional method signatures here
}
