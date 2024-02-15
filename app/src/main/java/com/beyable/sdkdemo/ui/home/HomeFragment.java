package com.beyable.sdkdemo.ui.home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.beyable.beyable_sdk.Beyable;
import com.beyable.beyable_sdk.models.BYHomeAttributes;
import com.beyable.beyable_sdk.models.BYVisitorInfos;
import com.beyable.sdkdemo.R;
import com.beyable.sdkdemo.databinding.FragmentHomeBinding;
import com.beyable.sdkdemo.utils.Cart;

import org.json.JSONObject;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private boolean isLogged;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Button logButton = binding.logButton;
        // Check if user is connected (we keep a flag on SharePreferences)
        SharedPreferences pref = getContext().getSharedPreferences("beyable_demo_preferences", 0);
        isLogged = pref.getBoolean("is_logged", false);
        handleButtonText(logButton);
        handleLoginStatus();

        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isLogged = !isLogged;
                SharedPreferences pref = getContext().getSharedPreferences("beyable_demo_preferences", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("is_logged", isLogged);
                editor.apply();

                handleButtonText(logButton);
                handleLoginStatus();
                try {
                    if (isLogged) {
                        JSONObject d = new JSONObject(pref.getString("cart", "{\"items\":[]}"));
                        Cart.getSharedInstance().fromJSONObject(d);
                    } else {
                        // Save the data from the cart
                        editor.putString("cart", Cart.getSharedInstance().toJSONObject().toString());
                        editor.apply();
                        Cart.getSharedInstance().eraseData();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        sendPageViewToBeyable(root);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void sendPageViewToBeyable(View rootView) {
        // CALL Beyable SDK to inform that we are viewing the home page
        BYHomeAttributes attributes = new BYHomeAttributes();
        Beyable.getSharedInstance().sendPageView(rootView, "/", attributes);
    }

    private void handleButtonText(Button button) {
        if (isLogged) {
            button.setText(getResources().getText(R.string.home_logout_button));
        } else {
            button.setText(getResources().getText(R.string.home_login_button));
        }
    }

    private void handleLoginStatus() {
        if (isLogged) {
            Beyable.getSharedInstance().setVisitorInfos(
                    new BYVisitorInfos(true, true, "Gol D. Marko", "smartphones"));
        } else {
            // The user is now logged out. Prevent Beyable
            Beyable.getSharedInstance().setVisitorInfos(null);
        }
    }

}