package com.example.finalisimofinalandroid;

import java.util.ArrayList;
import java.util.List;

public class CategoryRepository {

    private final List<Category> categoryList = new ArrayList<>();

    public CategoryRepository() {
        categoryList.clear();

        // Inicializa con categorías predeterminadas
        categoryList.add(new Category("Back-End"));
        categoryList.add(new Category("Front-End"));
        categoryList.add(new Category("Testing"));
    }

    public List<Category> getAllCategories() {
        return new ArrayList<>(categoryList);
    }

    public void insertCategory(Category category) {
        categoryList.add(category);
    }

}
