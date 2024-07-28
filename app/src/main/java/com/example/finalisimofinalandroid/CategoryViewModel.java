package com.example.finalisimofinalandroid;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class CategoryViewModel extends ViewModel {

    private final CategoryRepository categoryRepository;
    private final MutableLiveData<List<Category>> categoryList;

    public CategoryViewModel() {
        categoryRepository = new CategoryRepository();
        categoryList = new MutableLiveData<>(categoryRepository.getAllCategories());
    }

    public LiveData<List<Category>> getAllCategories() {
        return categoryList;
    }

    public void insertCategory(Category category) {
        categoryRepository.insertCategory(category);
        categoryList.setValue(categoryRepository.getAllCategories());
    }

}
