package com.example.newsfeed.utils;

import java.util.Locale;

public class Utils {
    public static String getCountry(){
        Locale locale = Locale.getDefault();
        String country = String.valueOf(locale.getCountry());
        return country.toLowerCase();
    }
}
