package org.yearup.service;

import org.springframework.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yearup.*;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryDao categoryDao;


    @Autowired
    public CategoryService(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }
   public Category getById(int categoryId){
       return  categoryDao.getById(categoryId);
   }

        public List<Category> getAllCategories(){
        return categoryDao.getAllCategories();
        }
}

