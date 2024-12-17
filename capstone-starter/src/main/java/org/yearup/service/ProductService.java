package org.yearup.service;


import org.springframework.stereotype.Service;
import org.yearup.models.Product;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    public List<Product> getProductsByCategoryId(int categoryId){
        return new ArrayList<>();
    }
}
