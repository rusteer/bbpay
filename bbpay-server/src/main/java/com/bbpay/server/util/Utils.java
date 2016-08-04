package com.bbpay.server.util;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static String generatePassword(String seedBase) {
        if (seedBase != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(seedBase.hashCode());
            while (sb.length() < 16) {
                sb.append(sb.toString().hashCode());
            }
            return sb.substring(0, 16);
        }
        return null;
    }
    public static boolean isMobileNO(String s) {
        if (s == null || s.trim().equals("")) {
            return false;
        } else {
            Matcher matcher = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$").matcher(s);
            return matcher.matches();
        }
    }
    public static String getStatackTrace(Throwable e) {
        String result = null;
        if (e != null) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw, true));
            result = sw.toString();
            try {
                sw.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        if (result != null) {
            StringBuilder sb = new StringBuilder();
            for (String line : result.split("\n")) {
                if (line.contains("com.bbpay")) {
                    sb.append(line).append("\n");
                }
            }
            result = sb.toString();
        }
        return result;
    }
}
