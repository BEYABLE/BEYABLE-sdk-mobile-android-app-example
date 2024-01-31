package com.beyable.sdkdemo.ui.product;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.beyable.beyable_sdk.Beyable;
import com.beyable.beyable_sdk.models.BYPage;
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

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(product.getTitle());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        sendPageViewToBeyable();
    }

    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return true;
    }


    private void sendPageViewToBeyable() {
        // CALL Beyable SDK to inform that we are viewing the home page
        Beyable.getSharedInstance().sendPageView(this, new BYPage(
                BYPage.BYPageType.PRODUCT,
                "https://dummy_app.com",
                "/"
        ));
    }
}
