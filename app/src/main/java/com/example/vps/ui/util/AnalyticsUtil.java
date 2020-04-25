package com.example.vps.ui.util;

import android.content.Context;

import com.google.firebase.analytics.FirebaseAnalytics;

public class AnalyticsUtil {

    private static FirebaseAnalytics firebaseAnalytics;

    public static FirebaseAnalytics getInstance(Context context){
        if(firebaseAnalytics == null){
            return FirebaseAnalytics.getInstance(context);
        } return firebaseAnalytics;
    }

}
