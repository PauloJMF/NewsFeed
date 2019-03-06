package com.example.newsfeed.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class Utils {
    public static String getCountry() {
        Locale locale = Locale.getDefault();
        String country = String.valueOf(locale.getCountry());
        return country.toLowerCase();
    }

    public static String getLanguage() {
        String[] supported = {"ar", "de", "en", "es", "fr", "he", "it", "nl", "no", "pt", "ru", "se", "ud", "zh"};
        Locale locale = Locale.getDefault();
        String language = locale.getLanguage();
        if (Arrays.asList(supported).contains(language)) return language;
        return "";
    }

    public static String getFormattedDate(String dateString) {
        if (dateString == null) {
            return "";
        }
        String[] separated = dateString.split("-");
        return separated[2].substring(0,2) + "/" + separated[1] + "/" + separated[0];
    }


}
