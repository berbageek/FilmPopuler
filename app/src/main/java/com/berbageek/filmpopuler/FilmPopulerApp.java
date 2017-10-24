package com.berbageek.filmpopuler;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by Muhammad Fiqri Muthohar on 10/24/17.
 */

public class FilmPopulerApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }
    }
}
