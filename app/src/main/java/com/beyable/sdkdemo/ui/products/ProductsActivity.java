package com.beyable.sdkdemo.ui.products;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.beyable.sdkdemo.databinding.ActivityMainBinding;

/**
 * Created by MarKinho on 29/01/2024.
 * <p>
 * Wisepear Techlab
 * All rights reserved
 **/

public class ProductsActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

}
