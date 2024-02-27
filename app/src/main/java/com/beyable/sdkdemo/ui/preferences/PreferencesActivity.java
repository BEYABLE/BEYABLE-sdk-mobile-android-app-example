package com.beyable.sdkdemo.ui.preferences;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.beyable.sdkdemo.R;
import com.beyable.sdkdemo.databinding.ActivityPreferencesBinding;


/**
 * Created by Gol D. Marko on 29/01/2024.
 * <p>
 **/

public class PreferencesActivity extends AppCompatActivity {

    private final static String LOG_TAG = PreferencesActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityPreferencesBinding binding = ActivityPreferencesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.title_settings));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setLogo(R.drawable.logo_beyable_small);
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return true;
    }

}
