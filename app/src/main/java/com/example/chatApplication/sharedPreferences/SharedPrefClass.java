package com.example.chatApplication.sharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.chatApplication.Constants;

public class SharedPrefClass {
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private static SharedPrefClass sharedPrefClass = null;

    public static SharedPrefClass getInstance(Context context) {
        if (sharedPrefClass == null) {
            sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREF_FILE, Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
            sharedPrefClass = new SharedPrefClass();
        }
        return sharedPrefClass;
    }

    public boolean isUserLoggedIn() {
        return sharedPreferences.getBoolean(Constants.PREF_KEY, false);
    }

    public String getCurrentUserEmail(){
        return sharedPreferences.getString(Constants.CURRENT_USER_EMAIL,"");
    }
    public void setLogin(String email) {
        editor.putBoolean(Constants.PREF_KEY, true);
        editor.putString(Constants.CURRENT_USER_EMAIL,email);
        editor.apply();
    }
    public void setLogout() {
//        editor.putBoolean(Constants.PREF_KEY, false);
//        editor.putString(Constants.CURRENT_USER_EMAIL,"");
        editor.clear();
        editor.apply();
    }


}
