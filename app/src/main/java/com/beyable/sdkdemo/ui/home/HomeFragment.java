package com.beyable.sdkdemo.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.beyable.beyable_sdk.Beyable;
import com.beyable.beyable_sdk.models.BYHomeAttributes;
import com.beyable.sdkdemo.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

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
        Beyable.getSharedInstance().sendPageView(getActivity(), "/", attributes);
    }

}