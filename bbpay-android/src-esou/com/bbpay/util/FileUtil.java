package com.bbpay.util;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import android.os.Environment;

public class FileUtil {
    private static String AD_PATH = "epay";
    private static String exterPath;
    public FileUtil() {}
    public static void deleteEpayFile(String s) {
        int i;
        MyLogger.info("==", new StringBuilder("删除文件").append(s).toString());
        String as[];
        File file1;
        try {
            File file = new File(s);
            file.exists();
            file.isDirectory();
            as = file.list();
        } catch (Exception exception) {
            return;
        }
        i = 0;
        while (i < as.length) {
            if (s.endsWith(File.separator)) {
                //L1_L1:
                file1 = new File(new StringBuilder(String.valueOf(s)).append(as[i]).toString());
            } else {
                //L2_L2:
                file1 = new File(new StringBuilder(String.valueOf(s)).append(File.separator).append(as[i]).toString());
            }
            if (file1.isFile()) file1.delete();
            if (file1.isDirectory()) {
                deleteEpayFile(new StringBuilder(String.valueOf(s)).append("/").append(as[i]).toString());
                delFolder(new StringBuilder(String.valueOf(s)).append("/").append(as[i]).toString());
            }
            i++;
        }
    }
    public static void delFolder(String s) {
        try {
            deleteEpayFile(s);
            new File(s.toString()).delete();
            return;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    public static String getExterPath() {
        if (exterPath == null) {
            exterPath = Environment.getExternalStorageDirectory().getPath();
            exterPath = new StringBuilder(String.valueOf(exterPath)).append("/").append(AD_PATH).toString();
        }
        File file = new File(exterPath);
        MyLogger.info("====", new StringBuilder().append(file.getAbsolutePath()).append(" , ").append(file.isDirectory()).append(" , ").append(file.exists()).toString());
        if (!file.exists() || file.isFile()) MyLogger.info("====", new StringBuilder().append(file.mkdir()).toString());
        return exterPath;
    }
    public static String getUserIdFromFile() {
        String s = null;
        try {
            BufferedReader bufferedreader = new BufferedReader(new FileReader(new File(new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().getPath()))
                    .append("/easoupay/userid.cfg").toString())));
            while ((s = bufferedreader.readLine()) != null) {
                if (StringUtils.isNotBlank(s)) {
                    break;
                }
            }
            bufferedreader.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return s;
    }
    public static boolean saveFlashFile(InputStream inputstream, String s) {
        if (inputstream == null || s == null || s.trim().equals("")) return false;
        FileOutputStream fileoutputstream;
        BufferedOutputStream bufferedoutputstream;
        byte abyte0[];
        File file = new File(s);
        if (file.exists()) file.delete();
        try {
            fileoutputstream = new FileOutputStream(s);
            bufferedoutputstream = new BufferedOutputStream(fileoutputstream);
            abyte0 = new byte[1024];
            int i;
            while ((i = inputstream.read(abyte0)) != -1) {
                bufferedoutputstream.write(abyte0, 0, i);
            }
            try {
                bufferedoutputstream.flush();
                bufferedoutputstream.close();
                fileoutputstream.close();
                inputstream.close();
            } catch (Exception exception) {
                exception.printStackTrace();
                MyLogger.info("====", new StringBuilder("复制异常").append(exception.getMessage()).toString());
                return false;
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }
    public static boolean saveUserIdFile(String s) {
        if (s == null || s.trim().equals("")) return false;
        FileWriter filewriter;
        try {
            String s1 = new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().getPath())).append("/easoupay/userid.cfg").toString();
            File file = new File(new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().getPath())).append("/easoupay").toString());
            if (!file.exists()) file.mkdir();
            File file1 = new File(s1);
            if (file1.exists()) file1.delete();
            filewriter = new FileWriter(s1, false);
        } catch (Exception exception) {
            exception.printStackTrace();
            MyLogger.info("====", new StringBuilder("写入user异常").append(exception.getMessage()).toString());
            return false;
        }
        if (filewriter != null) {
            try {
                filewriter.write(s);
                filewriter.flush();
                filewriter.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return true;
    }
}
