package com.beyable.sdkdemo.ui.xml.category;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.beyable.sdkdemo.R;

/**
 * Created by MarKinho on 07/08/2024.
 * <p>
 * Wisepear Techlab
 * All rights reserved
 **/

public class ProductTextsComponent extends ConstraintLayout {

    public ProductTextsComponent(@NonNull Context context) {
        super(context);
        injectLayout(context);
    }

    public ProductTextsComponent(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        injectLayout(context);
    }
    public ProductTextsComponent(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        injectLayout(context);
    }
    public ProductTextsComponent(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        injectLayout(context);
    }

    private void injectLayout(Context context) {
        ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.product_texts_component, this);
    }
}
