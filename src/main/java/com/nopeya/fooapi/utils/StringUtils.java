package com.nopeya.fooapi.utils;

public class StringUtils {

    public static boolean isNotBlank(String string) {
        return string != null && string.length() > 0;
    }
}
