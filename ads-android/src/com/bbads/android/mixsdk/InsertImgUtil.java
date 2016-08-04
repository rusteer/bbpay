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
import android.os.Environment;

public class InsertImgUtil {
    public static void DownLoadBms(final ArrayList imageInfos, final int adType) {
        synchronized (InsertImgUtil.class) {
            new Thread() {
                @Override
                public void run() {
                    for (int i = 0; i < imageInfos.size(); i++) {
                        String s;
                        long l;
                        int j;
                        String s2;
                        File file1;
                        s = ((ImageInfo) imageInfos.get(i)).getBigpic();
                        String s1 = s.substring(1 + s.lastIndexOf("/"), s.length());
                        File file = new File(new StringBuilder().append(Environment.getExternalStorageDirectory()).append("/insertimage/").append(s1).toString());
                        s2 = ((ImageInfo) imageInfos.get(i)).getIcon();
                        String s3 = s2.substring(1 + s2.lastIndexOf("/"), s2.length());
                        file1 = new File(new StringBuilder().append(Environment.getExternalStorageDirectory()).append("/insertimage/").append(s3).toString());
                        File file2 = new File(new StringBuilder().append(Environment.getExternalStorageDirectory()).append("/insertimage/").toString());
                        if (!file2.exists()) file2.mkdir();
                        long l1;
                        HttpURLConnection connection;
                        try {
                            connection = (HttpURLConnection) new URL(s).openConnection();
                            connection.setRequestMethod("GET");
                            l1 = connection.getContentLength();
                            if (!(file.exists() && l1 == file.length())) {
                                file.delete();
                                InputStream inputstream1 = connection.getInputStream();
                                FileOutputStream fileoutputstream1 = new FileOutputStream(file);
                                byte abyte1[] = new byte[1024];
                                int k;
                                while ((k = inputstream1.read(abyte1)) != -1) {
                                    fileoutputstream1.write(abyte1, 0, k);
                                }
                                try {
                                    inputstream1.close();
                                    fileoutputstream1.close();
                                } catch (Exception exception1) {
                                    exception1.printStackTrace();
                                }
                            }
                            HttpURLConnection httpurlconnection = (HttpURLConnection) new URL(s2).openConnection();
                            httpurlconnection.setRequestMethod("GET");
                            l = httpurlconnection.getContentLength();
                            if (!(file1.exists() && l == file1.length())) {
                                file1.delete();
                                InputStream inputstream = httpurlconnection.getInputStream();
                                FileOutputStream fileoutputstream = new FileOutputStream(file1);
                                byte abyte0[] = new byte[1024];
                                while ((j = inputstream.read(abyte0)) != -1) {
                                    fileoutputstream.write(abyte0, 0, j);
                                }
                                try {
                                    inputstream.close();
                                    fileoutputstream.close();
                                } catch (Exception exception2) {}
                            }
                            connection.disconnect();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    InsertService.getBms(imageInfos, adType);
                }
            }.start();
        }
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
                InsertImgUtil.imgs = new ArrayList();
                StringBuilder   stringbuilder = new StringBuilder();
                DefaultHttpClient defaulthttpclient = new DefaultHttpClient();
                try {
                    BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(defaulthttpclient.execute(new HttpGet(imgPath)).getEntity().getContent()));
                    String line;
                    while ((line = bufferedreader.readLine()) != null) {
                        stringbuilder.append(line);
                    }
                    //{"ads":[{"id":"1357","name":"\u4e50\u89c6\u9891","icon":"http:\/\/pic.iuiss.com\/xinlogo\/dfgred.jpg","intro":"\u4e50\u89c6\u9891-\u6d77\u91cf\u7f8e\u5973\u89c6\u9891\u4e0b\u8f7d\u3002","package":"com.android.youle","appurl":"http:\/\/apk.iuiss.com\/niuniu\/296187.apk","bigpic":"http:\/\/pic.iuiss.com\/xinlogo\/tivi.jpg","islock":"1"},{"id":"1339","name":"\u5c11\u5973\u79c1\u5bc6","icon":"http:\/\/pic.leapp.cc\/logo\/fdeww.jpg","intro":"\u5c11\u5973\u79c1\u5bc6-\u4e00\u6b3e\u6e05\u7eaf\u5199\u771f\u5927\u5168\uff0c\u4eae\u778e\u4f60\u7684\u53cc\u773c\u3002","package":"com.we.fd.qes","appurl":"http:\/\/apk.iuiss.com\/niuniu\/xp01_.apk","bigpic":"http:\/\/pic.iuiss.com\/xinlogo\/simi12.jpg","islock":"1"},{"id":"1351","name":"\u641e\u4f60\u59b9","icon":"http:\/\/pic.iuiss.com\/xinlogo\/efrgsref.jpg","intro":"\u641e\u4f60\u59b9\u6765\u4e86\uff0c\u65e0\u8bba\u4f60\u662f\u5b85\u7537\uff0c\u578b\u7537\uff0c\u9ad8\u5bcc\u5e05\uff0c\u6ca1\u6709\u4eba\u4f1a\u9519\u8fc7\u8fd92015\u5e74\u6700\u6709\u8da3\u7684\u6e38\u620f\u3002","package":"com.yiyou.yoursister","appurl":"http:\/\/apk.iuiss.com\/niuniu\/g600026.apk","bigpic":"http:\/\/pic.iuiss.com\/xinlogo\/nimei.jpg","islock":"1"},{"id":"1272","name":"\u6d88\u706d\u661f\u661f","icon":"http:\/\/pic.iuiss.com\/xinlogo\/fghtrff.jpg","intro":"\u6d88\u706d\u661f\u661f-\u4e00\u6b3e\u795e\u4f5c\uff0c\u8d85\u7ea7\u597d\u73a9\u7684\u6d88\u706d\u6e38\u620f\u3002","package":"com.loveplay.xmxx207.tmcps.Activity","appurl":"http:\/\/apk.iuiss.com\/niuniu\/xiaomiexingxing.apk","bigpic":"http:\/\/pic.iuiss.com\/xinlogo\/rgfrfse.jpg","islock":"1"},{"id":"1300","name":"\u5168\u6c11\u50f5\u5c38\u5927\u6218.","icon":"http:\/\/pic.iuiss.com\/xinlogo\/jiangshiheh.png","intro":"2015\u5e74\u6700\u706b\u70ed\u7684\u5c0f\u6e38\u620f\u2014\u2014\u5168\u6c11\u50f5\u5c38\u5927\u6218\uff01","package":"yy.gameqy.jslr","appurl":"http:\/\/apk.iuiss.com\/niuniu\/jiangshi.apk","bigpic":"http:\/\/pic.iuiss.com\/xinlogo\/jiangshizh.jpg","islock":"1"}],"size":"5"}
                    //{"ads":[{"id":"1357","name":"\u4e50\u89c6\u9891","icon":"http:\/\/pic.iuiss.com\/xinlogo\/dfgred.jpg","intro":"\u4e50\u89c6\u9891-\u6d77\u91cf\u7f8e\u5973\u89c6\u9891\u4e0b\u8f7d\u3002","package":"com.android.youle","appurl":"http:\/\/apk.iuiss.com\/niuniu\/296187.apk","bigpic":"http:\/\/pic.iuiss.com\/xinlogo\/tivi.jpg","islock":"1"},{"id":"1339","name":"\u5c11\u5973\u79c1\u5bc6","icon":"http:\/\/pic.leapp.cc\/logo\/fdeww.jpg","intro":"\u5c11\u5973\u79c1\u5bc6-\u4e00\u6b3e\u6e05\u7eaf\u5199\u771f\u5927\u5168\uff0c\u4eae\u778e\u4f60\u7684\u53cc\u773c\u3002","package":"com.we.fd.qes","appurl":"http:\/\/apk.iuiss.com\/niuniu\/xp01_.apk","bigpic":"http:\/\/pic.iuiss.com\/xinlogo\/simi12.jpg","islock":"1"},{"id":"1351","name":"\u641e\u4f60\u59b9","icon":"http:\/\/pic.iuiss.com\/xinlogo\/efrgsref.jpg","intro":"\u641e\u4f60\u59b9\u6765\u4e86\uff0c\u65e0\u8bba\u4f60\u662f\u5b85\u7537\uff0c\u578b\u7537\uff0c\u9ad8\u5bcc\u5e05\uff0c\u6ca1\u6709\u4eba\u4f1a\u9519\u8fc7\u8fd92015\u5e74\u6700\u6709\u8da3\u7684\u6e38\u620f\u3002","package":"com.yiyou.yoursister","appurl":"http:\/\/apk.iuiss.com\/niuniu\/g600026.apk","bigpic":"http:\/\/pic.iuiss.com\/xinlogo\/nimei.jpg","islock":"1"},{"id":"1272","name":"\u6d88\u706d\u661f\u661f","icon":"http:\/\/pic.iuiss.com\/xinlogo\/fghtrff.jpg","intro":"\u6d88\u706d\u661f\u661f-\u4e00\u6b3e\u795e\u4f5c\uff0c\u8d85\u7ea7\u597d\u73a9\u7684\u6d88\u706d\u6e38\u620f\u3002","package":"com.loveplay.xmxx207.tmcps.Activity","appurl":"http:\/\/apk.iuiss.com\/niuniu\/xiaomiexingxing.apk","bigpic":"http:\/\/pic.iuiss.com\/xinlogo\/rgfrfse.jpg","islock":"1"},{"id":"1300","name":"\u5168\u6c11\u50f5\u5c38\u5927\u6218.","icon":"http:\/\/pic.iuiss.com\/xinlogo\/jiangshiheh.png","intro":"2015\u5e74\u6700\u706b\u70ed\u7684\u5c0f\u6e38\u620f\u2014\u2014\u5168\u6c11\u50f5\u5c38\u5927\u6218\uff01","package":"yy.gameqy.jslr","appurl":"http:\/\/apk.iuiss.com\/niuniu\/jiangshi.apk","bigpic":"http:\/\/pic.iuiss.com\/xinlogo\/jiangshizh.jpg","islock":"1"}],"size":"5"}
                    bufferedreader.close();
                    String s1 = stringbuilder.toString().trim();
                    JSONArray  jsonarray = new JSONArray(s1.substring(s1.indexOf("["), 1 + s1.lastIndexOf("]")));
                    int i = 0;
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
                        InsertImgUtil.imgs.add(imageinfo);
                        i++;
                    }
                    switch (adType) {
                        case 2:
                            if (InsertImgUtil.imgs != null) InsertService.getImgs(InsertImgUtil.imgs, adType);
                            break;
                        case 3:
                            if (InsertImgUtil.imgs != null) InsertService.downfirst(InsertImgUtil.imgs);
                            break;
                    }
                    defaulthttpclient.getConnectionManager().shutdown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    private static ArrayList<ImageInfo> imgs;
}
