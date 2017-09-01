package com.helado.indicatorratingbar.utils;

import android.graphics.Color;

public class RateColorUtils {

    // Prevent instantiation
    private RateColorUtils() {}

    public static boolean isColorDark(int color) {
        double darkness = 0.2126* Color.red(color) + 0.7152*Color.green(color) + 0.0722*Color.blue(color);
        if(darkness > 128){
            return false; // It's a light color
        }else{
            return true; // It's a dark color
        }
    }
}
