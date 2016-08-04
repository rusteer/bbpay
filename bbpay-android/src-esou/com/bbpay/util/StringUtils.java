package com.bbpay.util;
public class StringUtils {
    public static boolean isBlank(String s) {
        return s == null || s.trim().length() == 0;
    }
    public static boolean isBlank(String... as) {
        for (String s : as) {
            if (isNotBlank(s)) return false;
        }
        return true;
    }
    public static boolean isNotBlank(String s) {
        return !isBlank(s);
    }
    public static boolean isNotBlank(String... as) {
        for (String s : as) {
            if (isBlank(s)) return false;
        }
        return true;
    }
}
