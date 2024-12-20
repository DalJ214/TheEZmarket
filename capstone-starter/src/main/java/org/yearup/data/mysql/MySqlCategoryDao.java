package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao {
    public MySqlCategoryDao(DataSource dataSource) {
        super(dataSource);


    }

    @Override
    public List<Category> getAllCategories() {
        // get all categories
        String sql = "SELECT * FROM categories;";
        List<Category> categories = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Category category = new Category();
                category.setCategoryId(resultSet.getInt("category_id"));
                category.setName(resultSet.getString("name"));
                category.setDescription(resultSet.getString("description"));
                categories.add(category);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching categories", e);
        }
        return categories;
    }

    @Override
    public Category getById(int categoryId) {
        // get category by id
        String sql = "SELECT * FROM categories WHERE category_id = ?";
        //Category category = null;
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, categoryId);

            ResultSet row = preparedStatement.executeQuery();

            if (row.next()) {
                return mapRow(row);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
            /*try (ResultSet resultSet = preparedStatement.executeQuery()){
                if (resultSet.next()){
                    category = new Category();
                    category.setCategoryId(resultSet.getInt("category_id"));
                    category.setName(resultSet.getString("name"));
                    category.setDescription(resultSet.getString("description"));
*/

          /*  } catch (SQLException e){
                e.printStackTrace();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return category;
    }*/

    @Override
    public Category create(Category category) {
        // create a new category
        String sql = "INSERT INTO categories (name, description) VALUES (?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, category.getName());
            preparedStatement.setString(2, category.getDescription());

            int rowsAffected = preparedStatement.executeUpdate();

            //checking if the category was inserted successfully
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);//getting the generated Id
                        category.setCategoryId(generatedId);
                        return category;
                    }
                }
            } else {
                throw new SQLException("Failed to create category, no rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error creating category", e);
        }
        return null;
    }

    @Override
    public Category insert(Category category) {
        Category createdCategory = null;
        String sql = "INSERT INTO categories (category_id, name, description) VALUES (?, ?, ?)";
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            statement.setInt(1, category.getCategoryId());
            statement.setString(2, category.getName());
            statement.setString(3, category.getDescription());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return getById(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting category", e);

        }
        return null;
    }
           /* ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()){
                createdCategory = getById(generatedKeys.getInt(1));
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return createdCategory;
    }*/

    @Override
    public Category update(int categoryId, Category category) {
        // update category
        String sql = "UPDATE categories SET name = ?, description = ? WHERE category_id = ?";

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);

            // Set the parameters for the update
            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());
            statement.setInt(3, categoryId);

            // Execute the update
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                // Return the updated category
                return getById(categoryId);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error updating category", e);
        }

        // Return null if the update failed
        return null;
    }

        /* int categoryIdPos= 0;
        int namePos = 0;
        int descriptionPos = 0;
        String updateParamStatement = "";
        if (category.getCategoryId() !=  0){
            categoryIdPos+=1;
            String comma = "";
            if (updateParamStatement.length() > 0){
                comma=",";
            }
            updateParamStatement+=comma+"category_id=?";

        }
        if (category.getName() != null){
            namePos+=categoryIdPos+1;
            updateParamStatement+=" name=? ";
        }
        if (category.getDescription() != null){
            descriptionPos+=categoryIdPos+namePos+1;
            updateParamStatement+=" description=?";

        }
        String sql = "UPDATE categories SET " + updateParamStatement + "WHERE category_id=?";
        try(Connection connection = getConnection()){
            PreparedStatement statement = connection.prepareStatement(sql);

            System.out.println(category);

            if (category.getCategoryId() != 0) {
                statement.setInt(categoryIdPos,category.getCategoryId());
            }
            if (category.getName()!= null){
                statement.setString(namePos, category.getName());

            }
            if (category.getDescription() != null){
                statement.setString(descriptionPos, category.getDescription());
            }
                statement.setInt(categoryIdPos+1, category.getCategoryId());

            statement.executeUpdate();

        }
        catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;*/



    @Override
    public boolean delete(int categoryId)
    {
        // delete category
       String sql = "DELETE FROM categories WHERE category_id=?";

       try(Connection connection = getConnection()){
           PreparedStatement statement = connection.prepareStatement(sql);
           statement.setInt(1,categoryId);

           int rowsAffected = statement.executeUpdate();

           if (rowsAffected > 0 ){
               return true;
           }


       }
       catch (SQLException e){
           e.printStackTrace();

       }
        return false;
    }

    private Category mapRow(ResultSet row) throws SQLException
    {
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

}
