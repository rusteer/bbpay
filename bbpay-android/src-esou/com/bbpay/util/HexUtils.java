package com.bbpay.util;
public class HexUtils {
    static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    public HexUtils() {}
    public static byte[] toByteArray(String s) {
        byte abyte0[] = new byte[s.length() / 2];
        int i = 0;
        int j = 0;
        do {
            if (j >= abyte0.length) return abyte0;
            int k = i + 1;
            int l = Character.digit(s.charAt(i), 16) << 4;
            i = k + 1;
            abyte0[j] = (byte) (l | Character.digit(s.charAt(k), 16));
            j++;
        } while (true);
    }
    public static String toHexString(byte abyte0[]) {
        char ac[] = new char[32];
        int i = 0;
        int j = 0;
        do {
            if (i >= 16) return new String(ac);
            byte byte0 = abyte0[i];
            int k = j + 1;
            ac[j] = hexDigits[0xf & byte0 >>> 4];
            j = k + 1;
            ac[k] = hexDigits[byte0 & 0xf];
            i++;
        } while (true);
    }
}
