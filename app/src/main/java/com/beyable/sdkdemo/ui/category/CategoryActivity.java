package com.beyable.sdkdemo.ui.category;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.beyable.beyable_sdk.Beyable;
import com.beyable.beyable_sdk.models.BYPage;
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

        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(category.getTitle());

        sendPageViewToBeyable();
    }


    private void sendPageViewToBeyable() {
        // CALL Beyable SDK to inform that we are viewing the home page
        Beyable.getSharedInstance().sendPageView(this, new BYPage(
                BYPage.BYPageType.CATEGORY,
                "https://dummy_app.com",
                "/"
        ));
    }

}
