package com.bbpay.android.utils;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class CommonUtil {
    public static String getAndroidId(Context context) {
        return Secure.getString(context.getContentResolver(), ANDROID_ID2);
    }
    public static String getCarrier(Context context) {
        if (context == null) return "";
        TelephonyManager manager = (TelephonyManager) context.getSystemService(PHONE);
        if (manager != null && manager.getSimState() == 5) return manager.getSimOperatorName();
        else return "";
    }
    public static String getRuntimeValue(Map<String, String> map, String variable) {
        if (map != null && map.size() > 0) {
            String variableName = searchEnclosed(variable, $, RIGHT);
            String variableValue = null;
            if (!TextUtils.isEmpty(variableName)) {
                variableValue = map.get(variableName);
            }
            if (variableValue != null) {
                variableValue = variable.replace($ + variableName + RIGHT, variableValue);
                if (variableValue.contains($) && variableValue.contains(RIGHT)) {//
                    variableValue = getRuntimeValue(map, variableValue);
                }
                variable = variableValue;
            }
        }
        return variable;
    }
    /**
     * @param target
     * @param searcher
     *  1:text(),text(是)
     *  2:enclosed(),
     *  3:regex(),regex(回复数字 ([0-9]*)`1)
     *  4:left(),
     *  5:right()
     * @return
     */
    public static String parseVariable(String target, String searcher) {
        try {
            if (target != null && !TextUtils.isEmpty(searcher) && searcher.endsWith(STRING4)) {
                for (int i = 0; i < supportedMethod.length; i++) {
                    String method = supportedMethod[i];
                    if (searcher.startsWith(method + STRING5)) {
                        String variable = searcher.substring(method.length() + 1, searcher.length() - 1);
                        switch (i) {
                            case 0:
                                return variable;
                            case 1:
                                return searchEnclosed(target, variable);
                            case 2:
                                return searchRegex(target, variable);
                            case 3:
                                return searchLeft(target, variable);
                            case 4:
                                return searchRight(target, variable);
                        }
                        break;
                    }
                }
            }
        } catch (Exception e) {
            MyLogger.error(e);
        }
        return null;
    }
    public static byte[] readFile(Context context, String name) {
        byte result[] = null;
        boolean exists = false;
        for (String f : context.fileList()) {
            if (name.equals(f)) {
                exists = true;
                break;
            }
        }
        if (exists) {
            FileInputStream stream = null;
            try {
                stream = context.openFileInput(name);
                result = new byte[stream.available()];
                stream.read(result);
            } catch (Exception e) {
                MyLogger.error(e);
            } finally {
                if (stream != null) try {
                    stream.close();
                } catch (Exception e) {}
            }
        }
        return result;
    }
    public static boolean saveFileValue(Context context, String s, byte abyte0[]) {
        boolean flag = false;
        if (abyte0 != null) {
            FileOutputStream fileoutputstream = null;
            try {
                fileoutputstream = context.openFileOutput(s, 0);
                fileoutputstream.write(abyte0);
                fileoutputstream.flush();
                flag = true;
            } catch (Exception e) {
                MyLogger.error(e);
            } finally {
                try {
                    fileoutputstream.close();
                } catch (IOException e) {
                    MyLogger.error(e);
                }
            }
        }
        return flag;
    }
    // left`right`matcherIndex
    private static String searchEnclosed(String target, String searcher) {
        if (target != null && searcher != null) {
            String left = null;
            String right = null;
            int matcherIndex = -1;
            String[] fields = searcher.split(SPLITTER);
            if (fields.length >= 2) {
                left = fields[0];
                right = fields[1];
                if (fields.length >= 3) {
                    matcherIndex = Integer.valueOf(fields[2].trim());
                }
            }
            return searchEnclosed(target, left, right, matcherIndex);
        }
        return null;
    }
    public static String searchEnclosed(String target, String left, String right) {
        return searchEnclosed(target, left, right, 0);
    }
    public static String searchEnclosed(String target, String left, String right, int matcherIndex) {
        if (!TextUtils.isEmpty(target) && !TextUtils.isEmpty(left) && !TextUtils.isEmpty(right)) {
            int index = 0;
            int leftPlace = 0;
            int rightPlace = 0;
            int fromPlace = 0;
            while (leftPlace >= 0 && rightPlace >= 0) {
                leftPlace = target.indexOf(left, fromPlace);
                if (leftPlace >= 0) {
                    fromPlace = leftPlace + left.length() + 1;
                    rightPlace = target.indexOf(right, fromPlace);
                    if (rightPlace > 0 && (matcherIndex == -1 || matcherIndex == index++)) {//
                        return target.substring(leftPlace + left.length(), rightPlace);
                    }
                    fromPlace = rightPlace + right.length() + 1;
                }
            }
        }
        return null;
    }
    private static String searchLeft(String target, String searcher) {
        if (target != null && searcher != null) {
            int place = target.indexOf(searcher);
            if (place >= 0) { return target.substring(0, place); }
        }
        return null;
    }
    // regex`groupIndex`matcherIndex
    private static String searchRegex(String target, String searcher) {
        String result = null;
        String regex = null;
        int matcherIndex = -1;
        int groupIndex = -1;
        if (searcher != null) {
            String[] fields = searcher.split(SPLITTER);
            if (fields.length >= 2) {
                regex = fields[0];
                groupIndex = Integer.valueOf(fields[1].trim());
                if (fields.length >= 3) {
                    matcherIndex = Integer.valueOf(fields[2].trim());
                }
            }
        }
        if (regex != null && groupIndex >= 0) {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(target);
            int index = 0;
            while (matcher.find()) {
                if (matcherIndex == -1 || index++ == matcherIndex) {
                    if (groupIndex <= matcher.groupCount()) {
                        result = matcher.group(groupIndex);
                    }
                    break;
                }
            }
        }
        return result;
    }
    private static String searchRight(String target, String searcher) {
        if (target != null && searcher != null) {
            int place = target.indexOf(searcher);
            if (place >= 0) { //
                return target.substring(place + searcher.length());
            }
        }
        return null;
    }
    public static void sleep(long milseconds) {
        try {
            Thread.sleep(milseconds);
        } catch (InterruptedException e) {
            MyLogger.error(e);
        }
    }
    public static JSONArray toJsonArray(String s) {
        JSONArray obj = null;
        if (s != null && s.trim().length() > 0) {
            try {
                obj = new JSONArray(s);
            } catch (JSONException e) {
                MyLogger.error(e);
            }
        }
        return obj;
    }
    public static JSONObject toJsonObject(String s) {
        JSONObject obj = null;
        if (s != null && s.trim().length() > 0) {
            try {
                obj = new JSONObject(s);
            } catch (JSONException e) {
                MyLogger.error(e);
            }
        }
        return obj;
    }
    public static String toString(Exception exception) {
        StringWriter sw = null;
        PrintWriter pw = null;
        String result = exception.getMessage();
        try {
            sw = new StringWriter();
            pw = new PrintWriter(sw);
            exception.printStackTrace(pw);
            result = sw.toString();
        } catch (Exception e) {
            MyLogger.error(e);
        } finally {
            try {
                sw.close();
            } catch (IOException e) {
                MyLogger.error(e);
            }
            pw.close();
        }
        return result;
    }
    private static final String STRING5 = "(";
    private static final String STRING4 = ")";
    private static String RIGHT = "}";
    private static final String $ = "${";
    private static final String ANDROID_ID2 = "android_id";
    public static String SMS_SEND = "android.provider.Telephony.SMS_SEND";
    public static String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static String PHONE = "phone";
    public static String SPLITTER = "`";
    public static boolean IS_REGISTER = false;
    /**
     * ["text","enclosed", "regex","left","right"]
     */
    private static String[] supportedMethod = { "text", "enclosed", "regex", "left", "right" };
}
