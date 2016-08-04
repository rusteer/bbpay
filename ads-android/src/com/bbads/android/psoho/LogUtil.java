// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
package com.bbads.android.psoho;
public class LogUtil {
    public static void i(String s, boolean flag) {
        i(s, String.valueOf(flag));
    }
    public static void i(String s, int j) {
        i(s, String.valueOf(j));
    }
    public static void i(String s, long l) {
        i(s, String.valueOf(l));
    }
    public static void i(String s, String s1) {}
    public LogUtil() {}
}
