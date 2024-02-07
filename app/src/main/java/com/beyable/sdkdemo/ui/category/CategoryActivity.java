package com.beyable.sdkdemo.ui.category;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.beyable.beyable_sdk.Beyable;
import com.beyable.beyable_sdk.models.BYCategoryAttributes;
import com.beyable.sdkdemo.R;
import com.beyable.sdkdemo.databinding.ActivityCategoryBinding;
import com.beyable.sdkdemo.models.Category;

/**
 * Created by MarKinho on 29/01/2024.
 * <p>
 * Wisepear Techlab
 * All rights reserved
 **/

public class CategoryActivity extends AppCompatActivity {

    public final static String CATEGORY_INTENT_KEY = "category_activity.category";

    private Category category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the data
        category = (Category) getIntent().getSerializableExtra(CATEGORY_INTENT_KEY);

        // Set the views
        ActivityCategoryBinding binding = ActivityCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(category.getTitle());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setLogo(R.drawable.logo_beyable_small);
        }

        sendPageViewToBeyable();
    }

    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return true;
    }

    private void sendPageViewToBeyable() {
        BYCategoryAttributes attributes = new BYCategoryAttributes();
        attributes.setName(category.getTitle());
        attributes.setTags(new String[]{category.getCategory()});
        Beyable.getSharedInstance().sendPageView(this, "category/"+category.getCategory(), attributes);
    }

}
