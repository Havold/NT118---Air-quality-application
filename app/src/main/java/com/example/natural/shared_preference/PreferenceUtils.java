package com.example.natural.shared_preference;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.natural.App;
import com.example.natural.App;


public class PreferenceUtils {
    private static final String USER_TOKEN = "USER_TOKEN";


    private static SharedPreferences preferences;

    public static synchronized void init() {
        preferences = PreferenceManager.getDefaultSharedPreferences(App.getInstance());
    }


    public static boolean isLogin() {
        return !getToken().isEmpty();
    }

    public static void saveToken(String token) {
        preferences.edit().putString(USER_TOKEN, token).apply();
    }

    public static String getToken() {
        return preferences.getString(USER_TOKEN, "");
    }

}
