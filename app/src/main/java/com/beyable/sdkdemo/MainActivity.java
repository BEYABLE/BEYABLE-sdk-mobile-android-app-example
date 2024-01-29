package com.beyable.sdkdemo;

import android.os.Bundle;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.beyable.beyable_sdk.Beyable;
import com.beyable.beyable_sdk.models.BYPage;
import com.beyable.sdkdemo.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_categories, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);


        // Init of Beyable SDK
        // Clef prod
        String prodKey = "aaaaaaaaa02a395426f4846fe8f7c478d6dc9c444";
        // Clef preprod
        String preProdKey = "aaaaaaaaa2703cf6e44624d9b81f15f14893d1d6a";
        Beyable.initInstance(getApplicationContext(), preProdKey);
        sendPageViewToBeyable();
    }


    private void sendPageViewToBeyable() {
        // Retrieve the root view of the activity
        final ViewGroup rootView = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);
        // CALL Beyable SDK to inform that we are viewing the home page
        try {
            Beyable.getSharedInstance().sendPageView(rootView, new BYPage(
                    BYPage.BYPageType.HOME,
                    "https://dummy_app.com",
                    "/"
            ));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}