package com.beyable.sdkdemo.ui.xml.category;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.beyable.sdkdemo.databinding.ActivityCategoryBinding;
import com.beyable.sdkdemo.models.Category;

/**
 * Created by Gol D. Marko on 29/01/2024.
 * <p>
 * Wisepear Techlab
 * All rights reserved
 **/

public class XmlCategoryActivity extends AppCompatActivity {

    public final static String CATEGORY_INTENT_KEY = "category_activity.category";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the data
        Category category = (Category) getIntent().getSerializableExtra(CATEGORY_INTENT_KEY);
        // Set the views
        ActivityCategoryBinding binding = ActivityCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Set Action bar
        if (getSupportActionBar() != null && category != null) {
            getSupportActionBar().setTitle(category.getTitle());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return true;
    }

}
