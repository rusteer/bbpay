package utils;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import utils.base64.Base64;

public class AES {
    public static String decode(String data, String password) {
        AES aes = new AES(password);
        byte[] crypted = Base64.decodeToBytes(data);
        return new String(aes.decrypt(crypted));
    }
    /**
     * "sfe023f_9fd&fwfl"
     *
     * @param s
     * @param password
     * @return
     */
    public static String encode(String data, String password) {
        AES aes = new AES(password);
        byte[] crypted = aes.encrypt(data.getBytes());
        return Base64.encode(crypted);
    }
    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        AES aes = new AES("");
        String password = "sfe023f_9fd&fwfl";
        String data = "你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊";
        System.out.println(encode(data, password));
        System.out.println(decode(encode(data, password), password));
        /*  byte[] crypted = aes.encrypt(data.getBytes());
          System.out.println(Base64.encode(crypted));
          System.out.println(new String(aes.decrypt(crypted)));
          String encodedString = Base64.encode(data);
          System.out.println(encodedString);
          System.out.println(Base64.decode(encodedString));*/
    }
    private IvParameterSpec ivSpec;
    private SecretKeySpec keySpec;
    public AES(String key) {
        try {
            byte[] keyBytes = key.getBytes();
            byte[] buf = new byte[16];
            for (int i = 0; i < keyBytes.length && i < buf.length; i++) {
                buf[i] = keyBytes[i];
            }
            keySpec = new SecretKeySpec(buf, "AES");
            ivSpec = new IvParameterSpec(keyBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public byte[] decrypt(byte[] crypted) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            return cipher.doFinal(crypted);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public byte[] encrypt(byte[] origData) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            return cipher.doFinal(origData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
