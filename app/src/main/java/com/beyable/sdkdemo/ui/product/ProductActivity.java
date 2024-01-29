package com.beyable.sdkdemo.ui.product;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.beyable.sdkdemo.databinding.ActivityProductBinding;
import com.beyable.sdkdemo.models.Product;


/**
 * Created by MarKinho on 29/01/2024.
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

        ActivityProductBinding binding = ActivityProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        toolbar.setTitle(product.getTitle());
    }


}
