package com.beyable.sdkdemo;

import android.app.Application;

import com.beyable.beyable_sdk.Beyable;

/**
 * Created by MarKinho on 31/01/2024.
 * <p>
 **/

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Init of Beyable SDK
        Beyable.initInstance(getApplicationContext(), "aaaaaaaaa2703cf6e44624d9b81f15f14893d1d6a");
    }
}
