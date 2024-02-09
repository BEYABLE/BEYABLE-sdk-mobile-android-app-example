package com.beyable.sdkdemo.ui.product;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.beyable.beyable_sdk.Beyable;
import com.beyable.beyable_sdk.models.BYProductAttributes;
import com.beyable.sdkdemo.R;
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

        ActivityProductBinding binding = ActivityProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(product.getTitle());
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
        // CALL Beyable SDK to inform that we are viewing a product page
        BYProductAttributes attributes = new BYProductAttributes();
        attributes.setReference(product.getId());
        attributes.setName(product.getTitle());
        attributes.setStock(product.getStock());
        attributes.setSellingPrice(product.getPrice());
        attributes.setThumbnailUrl(product.getThumbnail());
        attributes.setPriceBeforeDiscount(product.getDiscountPercentage());
        attributes.setTags(new String[]{
                product.getCategory(),
                product.getBrand()
        });
        Beyable.getSharedInstance().sendPageView(this, "product/"+product.getTitle(), attributes);
    }
}
