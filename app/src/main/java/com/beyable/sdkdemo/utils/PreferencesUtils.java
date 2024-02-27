package com.beyable.sdkdemo.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by MarKinho on 27/02/2024.
 * <p>
 **/

public class PreferencesUtils {

    private final static String URL_PREF_KEY        = "preferences_url_key";
    private final static String API_KEY_PREF_KEY    = "preferences_api_key_key";

    public final static String BEYABLE_PREPROD_URL      = "https://webapp-beyable-preprod.azurewebsites.net";
    public final static String BEYABLE_PREPROD_API_KEY  = "aaaaaaaaa2703cf6e44624d9b81f15f14893d1d6a";
    public final static String BEYABLE_PROD_URL         = "https://fd.front.activation.beyable.com";
    public final static String BEYABLE_PROD_API_KEY     = "aaaaaaaaa02a395426f4846fe8f7c478d6dc9c444";

    public static String[] getUrlAndApiKey(Context context) {
        SharedPreferences pref = context.getSharedPreferences("beyable_demo_preferences", 0);
        String url = pref.getString(URL_PREF_KEY, BEYABLE_PREPROD_URL);
        String apiKey = pref.getString(API_KEY_PREF_KEY, BEYABLE_PREPROD_API_KEY);
        return new String[]{url, apiKey};
    }

    public static void setUrlAndApiKey(Context context, String url, String apiKey) {
        SharedPreferences pref = context.getSharedPreferences("beyable_demo_preferences", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(URL_PREF_KEY, url);
        editor.putString(API_KEY_PREF_KEY, apiKey);
        editor.apply();
    }

}
