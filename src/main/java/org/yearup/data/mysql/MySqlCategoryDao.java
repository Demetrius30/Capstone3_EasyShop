package org.yearup.data.mysql;

import org.apache.ibatis.jdbc.SQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao {
    public MySqlCategoryDao(DataSource dataSource)
    {
        super(dataSource);
    }

    @Override
    public List<Category> getAllCategories(){
        List<Category> categories = new ArrayList<>();

        String query = "Select * From categories;";
        try(
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
        ){
            while (resultSet.next()){

                Category category = categoryShaper(resultSet);
                categories.add(category);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return categories;
    }

    @Override
    public Category getById(int categoryId) {
        String query = "Select * From categories Where category_id=?;";

        try(
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
        ){
            preparedStatement.setInt(1, categoryId);

            try(
                  ResultSet resultSet = preparedStatement.executeQuery();
        ){
                if(resultSet.next()){
                    return categoryShaper(resultSet);
                }else {
                    System.out.printf("Category Not Found");
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }return null;
    }

//    @Override// dont know if i need this method
//    public Category add(Category category) {
//        return null;
//    }

    @Override
    public Category create(Category category) {
        String query = "INSERT INTO categories(category_id, name, description) VALUES(?, ?, ?);";

        try(
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query,
                Statement.RETURN_GENERATED_KEYS);
        ){
            preparedStatement.setInt(1, category.getCategoryId());
            preparedStatement.setString(2, category.getName());
            preparedStatement.setString(3, category.getDescription());

            preparedStatement.executeUpdate();

            try(
                    ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            ){
                if(generatedKeys.next()){
                    int categoryId = generatedKeys.getInt(1);
                    category.setCategoryId(categoryId);
                    return category;
                }else{
                    System.out.println("Category Creation Failed");
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }return null;
    }

    @Override
    @PutMapping
    public void update(int categoryId, Category category){
        String query = "Update categories Set name=?, description=? Where category_id=?;";

        try(
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
        ){
            preparedStatement.setString(1, category.getName());
            preparedStatement.setString(2, category.getDescription());
            preparedStatement.setInt(3, categoryId);

            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    @DeleteMapping
    public void delete(int categoryId) {
       String query = "Delete From categories Where category_id=?;";

       try(
             Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
       ){
           preparedStatement.setInt(1, categoryId);
           preparedStatement.executeUpdate();
       }catch (SQLException e){
           e.printStackTrace();
           throw new RuntimeException("Failed to delete category with Id: " + categoryId, e);
       }
    }

    private Category mapRow(ResultSet row) throws SQLException {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        Category category = new Category()
        {{
            setCategoryId(categoryId);
            setName(name);
            setDescription(description);
        }};

        return category;
    }

    public Category categoryShaper(ResultSet resultSet) throws SQLException{

        int categoryId = resultSet.getInt("category_id");
        String categoryName = resultSet.getString("name");
        String categoryDesc = resultSet.getString("description");

        Category category = new Category(categoryId, categoryName, categoryDesc);
        return category;
    }

}
