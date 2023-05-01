package com.example.finalprojectgymapp.util;

import android.content.Context;
import android.content.SharedPreferences;

public class Utils {

    private static final String SET_AMOUNT_KEY = "set_amount";
    private static final String REP_AMOUNT_KEY = "rep_amount";
    private static final String USER_SETTINGS = "user_saved_settings";

    private static Utils instance;
    private SharedPreferences sharedPreferences;

    private Utils(Context context) {
        sharedPreferences = context.getSharedPreferences(USER_SETTINGS, Context.MODE_PRIVATE);
    }

    public static Utils getInstance(Context context) {
        if (null == instance) {
            instance = new Utils(context);
        }
        return instance;
    }

    public int getSetAmount() {
        return sharedPreferences.getInt(SET_AMOUNT_KEY, 3);
    }
    public boolean setSetAmount(int s) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(SET_AMOUNT_KEY, s);
        editor.commit();
        return true;
    }

    public int getRepAmount() {
        return sharedPreferences.getInt(REP_AMOUNT_KEY, 8);
    }
    public boolean setRepAmount(int r) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(REP_AMOUNT_KEY, r);
        editor.commit();
        return true;
    }
}
