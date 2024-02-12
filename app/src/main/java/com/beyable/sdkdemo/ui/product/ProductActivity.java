package com.beyable.sdkdemo.ui.product;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.beyable.sdkdemo.databinding.ActivityProductBinding;
import com.beyable.sdkdemo.models.Product;


/**
 * Created by Gol D. Marko on 29/01/2024.
 * <p>
 * Wisepear Techlab
 * All rights reserved
 **/

public class ProductActivity extends AppCompatActivity {

    private final static String LOG_TAG = ProductActivity.class.getSimpleName();
    public final static String PRODUCT_INTENT_KEY = "product_activity.product";

    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the data
        product = (Product) getIntent().getSerializableExtra(PRODUCT_INTENT_KEY);
        // GEt the binding
        ActivityProductBinding binding = ActivityProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Update ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(product.getTitle());
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
