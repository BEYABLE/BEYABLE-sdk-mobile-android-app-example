package com.beyable.sdkdemo.ui.cart;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.beyable.beyable_sdk.Beyable;
import com.beyable.beyable_sdk.models.BYCartAttributes;
import com.beyable.sdkdemo.R;
import com.beyable.sdkdemo.databinding.ActivityCartBinding;


/**
 * Created by MarKinho on 29/01/2024.
 * <p>
 **/

public class CartActivity extends AppCompatActivity {

    private final static String LOG_TAG = CartActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ActivityCartBinding binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Mon panier");
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
        BYCartAttributes cartAttributes = new BYCartAttributes();
        Beyable.getSharedInstance().sendPageView(this, "cart/", cartAttributes);
    }
}
