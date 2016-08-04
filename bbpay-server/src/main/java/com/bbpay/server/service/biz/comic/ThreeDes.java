package com.bbpay.server.service.biz.comic;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

public class ThreeDes {
    private Key key;
    private static final String KEY_ALGORITHM = "DESede";
    private static final String CIPHER_ALGORITHM = "DESede/ECB/PKCS5Padding";
    public ThreeDes() {}
    public ThreeDes(String keyStore) {
        this.toKey(keyStore);
    }
    void toKey(String keyText) {
        DESedeKeySpec dks;
        SecretKeyFactory factory = null;
        if (check(keyText)) {
            try {
                dks = new DESedeKeySpec(keyText.getBytes());
                factory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
                this.key = factory.generateSecret(dks);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public boolean check(String keyText) {
        boolean flag = false;
        if (keyText != null && !"".equals(keyText.trim())) {
            if (keyText.trim().length() >= 24) {
                flag = true;
            }
        }
        return flag;
    }
    public String getEncString(String inputText) {
        Cipher cipher = null;
        String outputText = null;
        try {
            cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, this.key);
            outputText = new String(Base64.encode(cipher.doFinal(inputText.getBytes("UTF8"))));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cipher = null;
        }
        return outputText;
    }
    public String getDesString(String inputText) {
        Cipher cipher = null;
        String outputText = null;
        try {
            cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, this.key);
            outputText = new String(cipher.doFinal(Base64.decode(inputText)));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cipher = null;
        }
        return outputText;
    }
}
