package hu.agta.rxwebsocket.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class AppGlobals extends Application {


    private static Context sContext;
    public static final String KEY_L_COUNT = "lcount";
    public static final String KEY_R_COUNT = "rcount";

    public static final String KEW_SWIPE = "swipe";

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
    }

    public static void swipeState(boolean type) {
        SharedPreferences sharedPreferences = getPreferenceManager();
        sharedPreferences.edit().putBoolean(KEW_SWIPE, type).commit();
    }

    public static boolean isSwipeEnabled() {
        SharedPreferences sharedPreferences = getPreferenceManager();
        return sharedPreferences.getBoolean(KEW_SWIPE, false);
    }

    public static SharedPreferences getPreferenceManager() {
        return getContext().getSharedPreferences("shared_prefs", MODE_PRIVATE);
    }

    public static void clearSettings() {
        SharedPreferences sharedPreferences = getPreferenceManager();
        sharedPreferences.edit().clear().commit();
    }

    public static void saveDataToSharedPreferences(String key, String value) {
        SharedPreferences sharedPreferences = getPreferenceManager();
        sharedPreferences.edit().putString(key, value).apply();
    }

    public static String getStringFromSharedPreferences(String key) {
        SharedPreferences sharedPreferences = getPreferenceManager();
        return sharedPreferences.getString(key, "");
    }


    public static Context getContext() {
        return sContext;
    }


}


