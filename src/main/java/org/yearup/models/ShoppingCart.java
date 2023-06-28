package org.yearup.models;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ShoppingCart {

    private int userId;
    private int productId;
    private int quantity;

    public ShoppingCart(int userId, int productId, int quantity, Map<Integer, ShoppingCartItem> items) {
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
        this.items = items;
    }


    public ShoppingCart(int userId, int productId, int quantity){

    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    private Map<Integer, ShoppingCartItem> items = new HashMap<>();

    public Map<Integer, ShoppingCartItem> getItems()
    {
        return items;
    }

    public void setItems(Map<Integer, ShoppingCartItem> items)
    {
        this.items = items;
    }

    public boolean contains(int productId)
    {
        return items.containsKey(productId);
    }

    public void add(ShoppingCartItem item)
    {
        items.put(item.getProductId(), item);
    }

    public ShoppingCartItem get(int productId)
    {
        return items.get(productId);
    }

    public BigDecimal getTotal()
    {
        BigDecimal total = items.values()
                                .stream()
                                .map(i -> i.getLineTotal())
                                .reduce( BigDecimal.ZERO, (lineTotal, subTotal) -> subTotal.add(lineTotal));

        return total;
    }

    @Override
    public String toString() {
        return "ShoppingCart{" +
                "userId=" + userId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                ", items=" + items +
                '}';
    }
}
