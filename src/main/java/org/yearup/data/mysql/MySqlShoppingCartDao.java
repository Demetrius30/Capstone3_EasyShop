package org.yearup.data.mysql;

import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Product;
import org.yearup.data.ProductDao;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao {
    public MySqlShoppingCartDao(DataSource dataSource) {super(dataSource);}

    @Override
    public ShoppingCart getByUserId(int userId){
        String query = "Select * From shopping_cart Where user_id=?;";

        try(
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
        ){
            preparedStatement.setInt(1, userId);

            try(
                    ResultSet resultSet = preparedStatement.executeQuery();
            ){
                if(resultSet.next()){
                    return shoppingCartShaper(resultSet);
                }else {
                    System.out.println("Id not found:(");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void saveShoppingCart(ShoppingCart shoppingCart) {

    }

    @Override
    public void deleteShoppingCart(int cartId) {

    }

    @Override
    public void addProductToCart(int userId, int productId) {
        //Looking if user has cart already
        ShoppingCart shoppingCart = getByUserId(userId);
        if(shoppingCart == null){
//            shoppingCart = new ShoppingCart(userId);
            saveShoppingCart(shoppingCart);
        }

        ShoppingCartItem existingProduct = shoppingCart.getItems().get(productId);
        if(existingProduct != null) {
            //if product exist increase by 1
            existingProduct.setQuantity(existingProduct.getQuantity() + 1);
//            update(shoppingCart.getProductId(), existingProduct);
        }
    }

//    @Override
//    public void update(int cartId, ShoppingCart shoppingCart) {
//
//    }

    @Override
    public List<ShoppingCart> getAllCart(){
        List<ShoppingCart> shoppingCarts = new ArrayList<>();

        String query = "Select * From shopping_cart";
        try(
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
        ){
            while (resultSet.next()){
                ShoppingCart shoppingCart = shoppingCartShaper(resultSet);
                shoppingCarts.add(shoppingCart);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }return shoppingCarts;
    }

    @Override
    public List<ShoppingCart> getShoppingCartById(int userId) {
        return null;
    }

    @Override
    public void addCartItem(int cartId, ShoppingCartItem shoppingCartItem) {

    }

    @Override
    public ShoppingCart create(ShoppingCart shoppingCart) {
        String query = "INSERT INTO shopping_cart(user_id, product_id, quantity) VALUES(?, ?, ?);";

        try(
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query,
                Statement.RETURN_GENERATED_KEYS)
        ){
            preparedStatement.setInt(1, shoppingCart.getUserId());
            preparedStatement.setInt(2, shoppingCart.getProductId());
            preparedStatement.setInt(3, shoppingCart.getQuantity());

            preparedStatement.executeUpdate();

            try(
                    ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            ){
                if(generatedKeys.next()){
                    int userId = generatedKeys.getInt(1);
                    shoppingCart.setUserId(userId);
                    return shoppingCart;
                }else{
                    System.out.println("Shopping Cart Creation Failed:(");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void update(int cartId, ShoppingCart shoppingCart) {
        String query = "Update shopping_cart Set user_id=?, product_id=?, quantity=?;";

        try(
               Connection connection = dataSource.getConnection();
               PreparedStatement preparedStatement = connection.prepareStatement(query);
        ){
            preparedStatement.setInt(1, shoppingCart.getUserId());
            preparedStatement.setInt(2, shoppingCart.getProductId());
            preparedStatement.setInt(3, shoppingCart.getQuantity());

            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int cartId) {
        String query = "Delete From shopping_cart Where user_id=?;";

        try(
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
        ){
            preparedStatement.setInt(1, cartId);

            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public ShoppingCart shoppingCartShaper(ResultSet resultSet)throws SQLException{
        int userId = resultSet.getInt("user_id");
        int productId = resultSet.getInt("product_id");
        int quantity = resultSet.getInt("quantity");

        ShoppingCart shoppingCart = new ShoppingCart(userId, productId, quantity);
        return shoppingCart;
    }

}
