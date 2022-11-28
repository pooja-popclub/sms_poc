package com.avdt.common;

import android.content.Context;

import com.avdt.readinboxsms.GlobalFunctions;

public class SharedPrefsGetSet {

    public static final String tokenPref = "token";
    public static final String phonePref = "phone";


    public static void setToken(Context mContext, String token) {
        GlobalFunctions.setSharedPrefs(mContext, tokenPref, token);
    }

    public static String getToken(Context mContext) {
        return GlobalFunctions.getSharedPrefs(mContext, tokenPref, "");
    }

    public static void setPhone(Context mContext, String token) {
        GlobalFunctions.setSharedPrefs(mContext, phonePref, token);
    }

    public static String getPhone(Context mContext) {
        return GlobalFunctions.getSharedPrefs(mContext, phonePref, "");
    }


}

