package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.CategoryDao;
import org.yearup.data.ProductDao;
import org.yearup.models.Category;
import org.yearup.models.Product;
//import org.yearup.service.CategoryService;

import java.util.List;

// add the annotations to make this a REST controller
// add the annotation to make this controller the endpoint for the following url
    // http://localhost:8080/categories
// add annotation to allow cross site origin requests
@RestController
@RequestMapping("categories")
@CrossOrigin(origins =  "http://localhost:8080")
public class CategoriesController {


    private CategoryDao categoryDao;

    private ProductDao productDao;


    // create an Autowired controller to inject the categoryDao and productDao@Autowired
    @Autowired
    public CategoriesController(CategoryDao categoryDao, ProductDao productDao) {
        this.categoryDao = categoryDao;
        this.productDao = productDao;
    }


    // add the appropriate annotation for a get action

    // @RequestMapping(path = "/categories", method = RequestMethod.GET)
    @GetMapping("")
    @PreAuthorize("permitAll()")
    public List<Category> getAll() {

        try {// find and return all categories
            List<Category> categories = categoryDao.getAllCategories();

            if (categories == null || categories.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No categories found");
            }
            return categories;

        } catch (
                Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad. ");
        }
    }


    // add the appropriate annotation for a get action
    //@RequestMapping(path = "categories/{id}", method = RequestMethod.GET)
    @GetMapping("{id}")
    @PreAuthorize("permitAll()")
    public Category getById(@PathVariable int id) {
        try {
            var category = categoryDao.getById(id);

            if (category == null)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);

            return category;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... Our bad.");
        }
        // get the category by id

    }

    // the url to return all products in category 1 would look like this
    // https://localhost:8080/categories/1/products
    @GetMapping("{id}/products")
    @PreAuthorize("permitAll()")
    public List<Product> getProductsById(@PathVariable int categoryId) {
        try {
            List<Product> products = productDao.listByCategoryId(categoryId);

            if (products == null)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);

            return products;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
        // get a list of product by categoryId

    }

    // add annotation to call this method for a POST action
    // add annotation to ensure that only an ADMIN can call this function
    //@RequestMapping(path = "/categories", method = RequestMethod.POST)
    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Category addCategory(@RequestBody Category category) {
        // insert the category
        try {
            Category createdCategory = this.categoryDao.insert(category);

            if (createdCategory == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category insertion failed");
            }
            return createdCategory;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }

    }

    // add annotation to call this method for a PUT (update) action - the url path must include the categoryId
    // add annotation to ensure that only an ADMIN can call this function
    //@RequestMapping(path = "/categories/{id}", method = RequestMethod.PUT)
    @PutMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Category updateCategory(@PathVariable int id, @RequestBody Category category) {
        // update the category by id
        try {
            Category updatedCategory = categoryDao.update(id, category);
            if (updatedCategory == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
            }
            return updatedCategory;

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }


    // add annotation to call this method for a DELETE action - the url path must include the categoryId
    // add annotation to ensure that only an ADMIN can call this function
    //@RequestMapping(path = "/categories/{id}", method = RequestMethod.DELETE)
    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteCategory(@PathVariable int id) {
        // delete the category by id
       try {
           boolean deleted = categoryDao.delete(id);
           if (!deleted){
               throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
           }
       }catch (Exception e){
           throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error during deletion ");
       }
       }
}

