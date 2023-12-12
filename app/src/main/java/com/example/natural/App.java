package com.example.natural;

import android.content.Context;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.example.natural.shared_preference.PreferenceUtils;


public class App extends MultiDexApplication {
    private static App instance = null;

    public static Context getInstance() {
        return instance;

    }

    @Override
    public void onCreate() {
        super.onCreate();
        getInstance(this);
        MultiDex.install(this);
        PreferenceUtils.init();
    }


    public static synchronized void getInstance(App app) {
        if (instance == null) instance = app;
    }
}
