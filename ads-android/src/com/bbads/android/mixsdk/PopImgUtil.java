package com.bbads.android.mixsdk;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class PopImgUtil {
    public static void clear() {
        bms.clear();
    }
    public static void DownLoadBms(final ArrayList imageInfos, final int adType) {
        new Thread() {
            @Override
            public void run() { //L2_L2:
                InputStream inputstream;
                FileOutputStream fileoutputstream;
                byte abyte0[];
                String s;
                File file;
                PopImgUtil.bms = new ArrayList<Bitmap>();
                s = ((ImageInfo) imageInfos.get(0)).getBigpic();
                String s1 = s.substring(1 + s.lastIndexOf("/"), s.length());
                file = new File(new StringBuilder("sdcard/popimage/").append(s1).toString());
                File file1 = new File("sdcard/popimage/");
                if (!file1.exists()) file1.mkdir();
                long l;
                try {
                    HttpURLConnection connection = (HttpURLConnection) new URL(s).openConnection();
                    connection.setRequestMethod("GET");
                    l = connection.getContentLength();
                    if (file.exists() && l == file.length()) {
                        //L1_L1:
                        Bitmap bitmap1 = BitmapFactory.decodeFile(file.getAbsolutePath());
                        PopImgUtil.bms.add(bitmap1);
                    } else {
                        file.delete();
                        inputstream = connection.getInputStream();
                        fileoutputstream = new FileOutputStream(file);
                        abyte0 = new byte[1024];
                        int i;
                        while ((i = inputstream.read(abyte0)) != -1) {
                            fileoutputstream.write(abyte0, 0, i);
                        }
                        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                        PopImgUtil.bms.add(bitmap);
                        inputstream.close();
                        fileoutputstream.close();
                    }
                    connection.disconnect();
                    PopUI.getBms(PopImgUtil.bms, adType);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }
        }.start();
    }
    public static Bitmap getIcon(String s) {
        Bitmap bitmap;
        try {
            HttpURLConnection httpurlconnection = (HttpURLConnection) new URL(s).openConnection();
            httpurlconnection.connect();
            bitmap = BitmapFactory.decodeStream(httpurlconnection.getInputStream());
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
        return bitmap;
    }
    public static void getImgs(final String imgPath, final int adType) {
        new Thread() {
            @Override
            public void run() {
                try {
                    PopImgUtil.imgs = new ArrayList<ImageInfo>();
                    StringBuilder stringbuilder = new StringBuilder();
                    DefaultHttpClient defaulthttpclient = new DefaultHttpClient();
                    BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(defaulthttpclient.execute(new HttpGet(imgPath)).getEntity().getContent()));
                    String s;
                    while ((s = bufferedreader.readLine()) != null) {
                        stringbuilder.append(s);
                    }
                    int i;
                    bufferedreader.close();
                    String s1 = stringbuilder.toString().trim();
                    JSONArray jsonarray = new JSONArray(s1.substring(s1.indexOf("["), 1 + s1.lastIndexOf("]")));
                    i = 0;
                    while (i < jsonarray.length()) {
                        ImageInfo imageinfo = new ImageInfo();
                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                        imageinfo.setId(jsonobject.getString("id"));
                        imageinfo.setName(jsonobject.getString("name"));
                        imageinfo.setIcon(jsonobject.getString("icon").replace("\\", ""));
                        imageinfo.setIntro(jsonobject.getString("intro"));
                        imageinfo.setPackageName(jsonobject.getString("package"));
                        imageinfo.setBigpic(jsonobject.getString("bigpic").replace("\\", ""));
                        imageinfo.setAppurl(jsonobject.getString("appurl").replace("\\", ""));
                        imageinfo.setIslock(jsonobject.getString("islock"));
                        PopImgUtil.imgs.add(imageinfo);
                        i++;
                    }
                    PopUI.getImgs(PopImgUtil.imgs, adType);
                    defaulthttpclient.getConnectionManager().shutdown();
                } catch (Exception exception) {
                    exception.printStackTrace();
                    return;
                }
            }
        }.start();
    }
    static ArrayList<Bitmap> bms = null;
    private static ArrayList<ImageInfo> imgs = null;
    public PopImgUtil() {}
}
