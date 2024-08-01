package com.beyable.sdkdemo.ui.xml.categories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class XmlCategoriesViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public XmlCategoriesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is categories fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}