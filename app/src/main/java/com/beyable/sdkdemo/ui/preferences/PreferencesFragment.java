package com.beyable.sdkdemo.ui.preferences;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.beyable.beyable_sdk.Beyable;
import com.beyable.beyable_sdk.models.BYGenericAttributes;
import com.beyable.sdkdemo.MainApplication;
import com.beyable.sdkdemo.R;
import com.beyable.sdkdemo.databinding.FragmentPreferencesBinding;
import com.beyable.sdkdemo.utils.PreferencesUtils;

import java.util.Arrays;
import java.util.List;

public class PreferencesFragment extends Fragment {

    protected final static String LOG_TAG = PreferencesFragment.class.getSimpleName();

    private FragmentPreferencesBinding binding;

    private Button validateButton;
    private boolean firstSelection = true;
    private int posSelected;
    private String urlSelected, keySelected;
    private TextView urlView, keyView;
    private EditText urlEditView, keyEditView;

    public View onCreateView(@NonNull LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPreferencesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        String[] urlAndKey = PreferencesUtils.getUrlAndApiKey(getContext());
        urlSelected = urlAndKey[0];
        keySelected = urlAndKey[1];

        urlView         = binding.urlView;
        keyView         = binding.keyView;
        urlEditView     = binding.urlEditView;
        urlEditView.getBackground().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        keyEditView     = binding.keyEditView;
        keyEditView.getBackground().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);

        Spinner spinner = binding.urlSpinnerView;
        spinner.getBackground().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);


        // Create a list to display in the Spinner
        final List<String> mList = Arrays.asList("Pre-prod", "Production", "Custom");
        ArrayAdapter<String> adapter =new ArrayAdapter<String>(
                getActivity(),
                R.layout.spinner_text,
                mList
        );
        // Specify the layout to use when the list of choices appears.
        adapter.setDropDownViewResource(R.layout.spinner_text);
        // Apply the adapter to the spinner.
        spinner.setAdapter(adapter);
        if (urlSelected.equals(PreferencesUtils.BEYABLE_PREPROD_URL) &&
            keySelected.equals(PreferencesUtils.BEYABLE_PREPROD_API_KEY)) {
            posSelected = 0;
            hideCustomField();
        } else if (urlSelected.equals(PreferencesUtils.BEYABLE_PROD_URL) &&
                keySelected.equals(PreferencesUtils.BEYABLE_PROD_API_KEY)) {
            posSelected = 1;
            hideCustomField();
        } else {
            posSelected = 2;
            showCustomFields();
        }
        spinner.setSelection(posSelected);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = mList.get(i);
                onUrlItemSelected(i, selected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        validateButton = binding.validateButton;

        urlView.setText(urlSelected);
        keyView.setText(keySelected);
        urlEditView.setText(urlSelected);
        keyEditView.setText(keySelected);


        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // We call Beyable only after the view as been created
        sendPageViewToBeyable();
    }

    private void sendPageViewToBeyable() {
        BYGenericAttributes attributes = new BYGenericAttributes();
        Beyable.getSharedInstance().sendPageView(getView(), "preferences/", attributes);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void onUrlItemSelected(int pos, String itemName) {
        if (!firstSelection) {
            validateButton.setVisibility(View.VISIBLE);
            validateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemValidate();
                }
            });
        }
        firstSelection = false;
        posSelected = pos;
        if (posSelected == 0) {
            urlSelected = PreferencesUtils.BEYABLE_PREPROD_URL;
            keySelected = PreferencesUtils.BEYABLE_PREPROD_API_KEY;
            hideCustomField();
        } else if (posSelected == 1){
            urlSelected = PreferencesUtils.BEYABLE_PROD_URL;
            keySelected = PreferencesUtils.BEYABLE_PROD_API_KEY;
            hideCustomField();
        } else {
            showCustomFields();
        }
        urlView.setText(urlSelected);
        keyView.setText(keySelected);
    }

    private void showCustomFields() {
        urlEditView.setVisibility(View.VISIBLE);
        keyEditView.setVisibility(View.VISIBLE);

        urlView.setVisibility(View.GONE);
        keyView.setVisibility(View.GONE);
    }

    private void hideCustomField() {
        urlView.setVisibility(View.VISIBLE);
        keyView.setVisibility(View.VISIBLE);

        urlEditView.setVisibility(View.GONE);
        keyEditView.setVisibility(View.GONE);
    }

    private void itemValidate() {
        try {
            if (urlEditView.getVisibility() == View.VISIBLE) {
                urlSelected = urlEditView.getText().toString();
                keySelected = keyEditView.getText().toString();
            }
            PreferencesUtils.setUrlAndApiKey(getContext(), urlSelected, keySelected);
            Toast.makeText(getContext(), getString(R.string.preferences_restart_app), Toast.LENGTH_LONG).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ((MainApplication) getActivity().getApplication()).triggerRebirth(getActivity());
                }
            }, 2000);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}

