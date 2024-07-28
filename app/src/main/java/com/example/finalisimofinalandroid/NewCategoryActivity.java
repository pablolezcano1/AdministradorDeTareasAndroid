package com.example.finalisimofinalandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashSet;
import java.util.Set;

public class NewCategoryActivity extends AppCompatActivity {

    private EditText editTextCategoryName;
    private CategoryViewModel categoryViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_category);

        editTextCategoryName = findViewById(R.id.editTextCategoryName);
        Button buttonSaveCategory = findViewById(R.id.buttonSaveCategory);

        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        buttonSaveCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCategory();
            }
        });
    }

    private void saveCategory() {
        String categoryName = editTextCategoryName.getText().toString();

        if (!categoryName.isEmpty()) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = prefs.edit();
            Set<String> categories = prefs.getStringSet("categories", new HashSet<String>());
            categories.add(categoryName);
            editor.putStringSet("categories", categories);
            editor.apply();

            Intent resultIntent = new Intent();
            resultIntent.putExtra("NEW_CATEGORY", categoryName);
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    }


}
