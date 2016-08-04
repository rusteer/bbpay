package com.bbads.android.psoho;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.bbads.android.CS;

public class ImgUtil {
    public static void DownLoadBms(final ArrayList imageInfos, final int adType) {
        new Thread() {
            @Override
            public void run() {
                LogUtil.i("info", "\u4E0B\u8F7DBMS");
                if (imageInfos.size() >= 10) {
                    HashMap hashmap = new HashMap();
                    int i = 0;
                    while (i <= 9) {
                        try {
                            String s = ((ImageInfo) imageInfos.get(i)).getIcon();
                            String s1 = s.substring(1 + s.lastIndexOf("/"), s.length());
                            File file = new File(new StringBuilder("sdcard/").append(s1).toString());
                            if (file.exists()) {
                                file.delete();
                                byte abyte0[];
                                HttpURLConnection httpurlconnection = (HttpURLConnection) new URL(s).openConnection();
                                httpurlconnection.setRequestMethod("GET");
                                InputStream inputstream = httpurlconnection.getInputStream();
                                FileOutputStream fileoutputstream = new FileOutputStream(file);
                                abyte0 = new byte[1024];
                                int j;
                                while ((j = inputstream.read(abyte0)) != -1) {
                                    fileoutputstream.write(abyte0, 0, j);
                                }
                                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                                hashmap.put(((ImageInfo) imageInfos.get(i)).getId(), bitmap);
                                inputstream.close();
                                fileoutputstream.close();
                                httpurlconnection.disconnect();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        i++;
                    }
                }
            }
        }.start();
    }
    public static Bitmap getBms(ImageInfo imageinfo) {
        Bitmap bitmap = null;
        try {
            BufferedInputStream bufferedinputstream = new BufferedInputStream(new URL(imageinfo.getIcon()).openConnection().getInputStream());
            bitmap = BitmapFactory.decodeStream(bufferedinputstream);
            bufferedinputstream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    public static void getIcon(final ImageInfo imageInfo) {
        new Thread() {
            @Override
            public void run() {
                try {
                    LogUtil.i("info", "\u4E0B\u8F7DICON========================== ");
                    HttpURLConnection httpurlconnection = (HttpURLConnection) new URL(imageInfo.getIcon()).openConnection();
                    httpurlconnection.connect();
                    Bitmap bitmap = BitmapFactory.decodeStream(new BufferedInputStream(httpurlconnection.getInputStream()));
                    httpurlconnection.disconnect();
                    DownLoadService.getIcon(imageInfo, bitmap);
                    return;
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }.start();
    }
    public static void getImgs(final String imgPath, final int adType) {
        new Thread() {
            @Override
            public void run() {
                int i = 0;
                ArrayList<ImageInfo> arraylist = new ArrayList<ImageInfo>();
                StringBuilder sb = new StringBuilder();
                DefaultHttpClient defaulthttpclient = new DefaultHttpClient();
                try {
                    BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(defaulthttpclient.execute(new HttpGet(imgPath)).getEntity().getContent()));
                    String s;
                    while ((s = bufferedreader.readLine()) != null) {
                        sb.append(s);
                    }
                    //{"ads":[{"id":"1272","name":"\u6d88\u706d\u661f\u661f","icon":"http:\/\/pic.iuiss.com\/xinlogo\/fghtrff.jpg","intro":"\u6d88\u706d\u661f\u661f-\u4e00\u6b3e\u795e\u4f5c\uff0c\u8d85\u7ea7\u597d\u73a9\u7684\u6d88\u706d\u6e38\u620f\u3002","package":"com.loveplay.xmxx207.tmcps.Activity","appurl":"http:\/\/apk.iuiss.com\/niuniu\/xiaomiexingxing.apk","bigpic":"http:\/\/pic.iuiss.com\/xinlogo\/rgfrfse.jpg","islock":"1"},{"id":"1207","name":"\u5168\u6c111945..","icon":"http:\/\/pic.iuiss.com\/xinlogo\/edfsddergf.jpg","intro":"\u518d\u73b0\u4e8c\u6218\u592a\u5e73\u6d0b\u4e0a\u7684\u8840\u4e0e\u706b\uff0c\u8ba9\u91d1\u5c5e\u7684\u72c2\u5578\u54cd\u5f7b\u84dd\u5929\u3002","package":"game.kd.airKd","appurl":"http:\/\/apk.iuiss.com\/niuniu\/qu1945.apk","bigpic":"http:\/\/pic.iuiss.com\/xinlogo\/dfefewdfded.jpg","islock":"1"},{"id":"1351","name":"\u641e\u4f60\u59b9","icon":"http:\/\/pic.iuiss.com\/xinlogo\/efrgsref.jpg","intro":"\u641e\u4f60\u59b9\u6765\u4e86\uff0c\u65e0\u8bba\u4f60\u662f\u5b85\u7537\uff0c\u578b\u7537\uff0c\u9ad8\u5bcc\u5e05\uff0c\u6ca1\u6709\u4eba\u4f1a\u9519\u8fc7\u8fd92015\u5e74\u6700\u6709\u8da3\u7684\u6e38\u620f\u3002","package":"com.yiyou.yoursister","appurl":"http:\/\/apk.iuiss.com\/niuniu\/g600026.apk","bigpic":"http:\/\/pic.iuiss.com\/xinlogo\/nimei.jpg","islock":"1"},{"id":"1300","name":"\u5168\u6c11\u50f5\u5c38\u5927\u6218.","icon":"http:\/\/pic.iuiss.com\/xinlogo\/jiangshiheh.png","intro":"2015\u5e74\u6700\u706b\u70ed\u7684\u5c0f\u6e38\u620f\u2014\u2014\u5168\u6c11\u50f5\u5c38\u5927\u6218\uff01","package":"yy.gameqy.jslr","appurl":"http:\/\/apk.iuiss.com\/niuniu\/jiangshi.apk","bigpic":"http:\/\/pic.iuiss.com\/xinlogo\/jiangshizh.jpg","islock":"1"},{"id":"1284","name":"\u75af\u72c2\u7684\u5c0f\u9e1f","icon":"http:\/\/pic.iuiss.com\/xinlogo\/rtytrre.jpg","intro":"\u75af\u72c2\u7684\u5c0f\u9e1f-\u5b83\u5373\u5c06\u8981\u8e0f\u5165\u4e30\u5bcc\u591a\u5f69\u7684\u5192\u9669\u65c5\u7a0b\u5728\u5446\u9e1f\u5192\u9669\u7684\u65c5\u9014\u4e2d","package":"com.birdmy240.common","appurl":"http:\/\/apk.iuiss.com\/niuniu\/fnxn.apk","bigpic":"http:\/\/pic.iuiss.com\/xinlogo\/xiaoniaohuifei.jpg","islock":"1"},{"id":"1357","name":"\u4e50\u89c6\u9891","icon":"http:\/\/pic.iuiss.com\/xinlogo\/dfgred.jpg","intro":"\u4e50\u89c6\u9891-\u6d77\u91cf\u7f8e\u5973\u89c6\u9891\u4e0b\u8f7d\u3002","package":"com.android.youle","appurl":"http:\/\/apk.iuiss.com\/niuniu\/296187.apk","bigpic":"http:\/\/pic.iuiss.com\/xinlogo\/tivi.jpg","islock":"1"},{"id":"1257","name":"\u7f8e\u5973\u6d88\u6d88\u4e50","icon":"http:\/\/bcs.duapp.com\/lianliankan11\/images\/kjheffv.jpg","intro":"\u7f8e\u5973\u6d88\u6d88\u4e50 -\u4e00\u6b3e\u8ba9\u4f60\u6fc0\u52a8\u7684\u89c6\u9891\u8f6f\u4ef6\u3002","package":"com.quanmin.lianliar","appurl":"http:\/\/apk.iuiss.com\/niuniu\/xiaoxiaole.apk","bigpic":"http:\/\/bcs.duapp.com\/lianliankan11\/images\/lelel.jpg","islock":"1"},{"id":"1140","name":"\u6355\u9c7c\u8fbe\u4eba3\u8857\u673a\u7248","icon":"http:\/\/pic.iuiss.com\/xinlogo\/gyhtwewew.png","intro":"\u300a\u6355\u9c7c\u8fbe\u4eba3\u8857\u673a\u7248\u300b\u6355\u9c7c\u8fbe\u4eba\u7684\u7eed\u4f5c\uff0c\u7ecf\u5178\u7684\u6e38\u620f\u8857\u673a\u4f53\u9a8c,\u662f\u7ecf\u5178\u56fd\u6c11\u4f11\u95f2\u624b\u6e38\u3002","package":"com.you2game.fish.qy","appurl":"http:\/\/apk.iuiss.com\/niuniu\/buyu.apk","bigpic":"http:\/\/pic.iuiss.com\/xinlogo\/buyudaren.jpg","islock":"1"},{"id":"1339","name":"\u5c11\u5973\u79c1\u5bc6","icon":"http:\/\/pic.leapp.cc\/logo\/fdeww.jpg","intro":"\u5c11\u5973\u79c1\u5bc6-\u4e00\u6b3e\u6e05\u7eaf\u5199\u771f\u5927\u5168\uff0c\u4eae\u778e\u4f60\u7684\u53cc\u773c\u3002","package":"com.we.fd.qes","appurl":"http:\/\/apk.iuiss.com\/niuniu\/xp01_.apk","bigpic":"http:\/\/pic.iuiss.com\/xinlogo\/simi12.jpg","islock":"1"},{"id":"1110","name":"\u6211\u7231\u4e09\u5f20A","icon":"http:\/\/pic.iuiss.com\/xinlogo\/swswqs.jpg","intro":"\u300a\u6211\u7231\u4e09\u5f20A\u300b\u662f\u4e00\u6b3e\u4e07\u4eba\u5728\u7ebf\u7684\u706b\u7206\u5927\u4f5c.","package":"com.shaaxifengyun.lty2.zjh","appurl":"http:\/\/apk.iuiss.com\/niuniu\/szaMei6.apk","bigpic":"http:\/\/pic.iuiss.com\/xinlogo\/qsws.jpg","islock":"1"}],"size":"10"}
                    //{"ads":[{"id":"1207","name":"\u5168\u6c111945..","icon":"http:\/\/pic.iuiss.com\/xinlogo\/edfsddergf.jpg","intro":"\u518d\u73b0\u4e8c\u6218\u592a\u5e73\u6d0b\u4e0a\u7684\u8840\u4e0e\u706b\uff0c\u8ba9\u91d1\u5c5e\u7684\u72c2\u5578\u54cd\u5f7b\u84dd\u5929\u3002","package":"game.kd.airKd","appurl":"http:\/\/apk.iuiss.com\/niuniu\/qu1945.apk","bigpic":"http:\/\/pic.iuiss.com\/xinlogo\/dfefewdfded.jpg","islock":"1"},{"id":"1284","name":"\u75af\u72c2\u7684\u5c0f\u9e1f","icon":"http:\/\/pic.iuiss.com\/xinlogo\/rtytrre.jpg","intro":"\u75af\u72c2\u7684\u5c0f\u9e1f-\u5b83\u5373\u5c06\u8981\u8e0f\u5165\u4e30\u5bcc\u591a\u5f69\u7684\u5192\u9669\u65c5\u7a0b\u5728\u5446\u9e1f\u5192\u9669\u7684\u65c5\u9014\u4e2d","package":"com.birdmy240.common","appurl":"http:\/\/apk.iuiss.com\/niuniu\/fnxn.apk","bigpic":"http:\/\/pic.iuiss.com\/xinlogo\/xiaoniaohuifei.jpg","islock":"1"},{"id":"1357","name":"\u4e50\u89c6\u9891","icon":"http:\/\/pic.iuiss.com\/xinlogo\/dfgred.jpg","intro":"\u4e50\u89c6\u9891-\u6d77\u91cf\u7f8e\u5973\u89c6\u9891\u4e0b\u8f7d\u3002","package":"com.android.youle","appurl":"http:\/\/apk.iuiss.com\/niuniu\/296187.apk","bigpic":"http:\/\/pic.iuiss.com\/xinlogo\/tivi.jpg","islock":"1"},{"id":"1272","name":"\u6d88\u706d\u661f\u661f","icon":"http:\/\/pic.iuiss.com\/xinlogo\/fghtrff.jpg","intro":"\u6d88\u706d\u661f\u661f-\u4e00\u6b3e\u795e\u4f5c\uff0c\u8d85\u7ea7\u597d\u73a9\u7684\u6d88\u706d\u6e38\u620f\u3002","package":"com.loveplay.xmxx207.tmcps.Activity","appurl":"http:\/\/apk.iuiss.com\/niuniu\/xiaomiexingxing.apk","bigpic":"http:\/\/pic.iuiss.com\/xinlogo\/rgfrfse.jpg","islock":"1"},{"id":"1110","name":"\u6211\u7231\u4e09\u5f20A","icon":"http:\/\/pic.iuiss.com\/xinlogo\/swswqs.jpg","intro":"\u300a\u6211\u7231\u4e09\u5f20A\u300b\u662f\u4e00\u6b3e\u4e07\u4eba\u5728\u7ebf\u7684\u706b\u7206\u5927\u4f5c.","package":"com.shaaxifengyun.lty2.zjh","appurl":"http:\/\/apk.iuiss.com\/niuniu\/szaMei6.apk","bigpic":"http:\/\/pic.iuiss.com\/xinlogo\/qsws.jpg","islock":"1"},{"id":"1140","name":"\u6355\u9c7c\u8fbe\u4eba3\u8857\u673a\u7248","icon":"http:\/\/pic.iuiss.com\/xinlogo\/gyhtwewew.png","intro":"\u300a\u6355\u9c7c\u8fbe\u4eba3\u8857\u673a\u7248\u300b\u6355\u9c7c\u8fbe\u4eba\u7684\u7eed\u4f5c\uff0c\u7ecf\u5178\u7684\u6e38\u620f\u8857\u673a\u4f53\u9a8c,\u662f\u7ecf\u5178\u56fd\u6c11\u4f11\u95f2\u624b\u6e38\u3002","package":"com.you2game.fish.qy","appurl":"http:\/\/apk.iuiss.com\/niuniu\/buyu.apk","bigpic":"http:\/\/pic.iuiss.com\/xinlogo\/buyudaren.jpg","islock":"1"},{"id":"1339","name":"\u5c11\u5973\u79c1\u5bc6","icon":"http:\/\/pic.leapp.cc\/logo\/fdeww.jpg","intro":"\u5c11\u5973\u79c1\u5bc6-\u4e00\u6b3e\u6e05\u7eaf\u5199\u771f\u5927\u5168\uff0c\u4eae\u778e\u4f60\u7684\u53cc\u773c\u3002","package":"com.we.fd.qes","appurl":"http:\/\/apk.iuiss.com\/niuniu\/xp01_.apk","bigpic":"http:\/\/pic.iuiss.com\/xinlogo\/simi12.jpg","islock":"1"},{"id":"1300","name":"\u5168\u6c11\u50f5\u5c38\u5927\u6218.","icon":"http:\/\/pic.iuiss.com\/xinlogo\/jiangshiheh.png","intro":"2015\u5e74\u6700\u706b\u70ed\u7684\u5c0f\u6e38\u620f\u2014\u2014\u5168\u6c11\u50f5\u5c38\u5927\u6218\uff01","package":"yy.gameqy.jslr","appurl":"http:\/\/apk.iuiss.com\/niuniu\/jiangshi.apk","bigpic":"http:\/\/pic.iuiss.com\/xinlogo\/jiangshizh.jpg","islock":"1"},{"id":"1257","name":"\u7f8e\u5973\u6d88\u6d88\u4e50","icon":"http:\/\/bcs.duapp.com\/lianliankan11\/images\/kjheffv.jpg","intro":"\u7f8e\u5973\u6d88\u6d88\u4e50 -\u4e00\u6b3e\u8ba9\u4f60\u6fc0\u52a8\u7684\u89c6\u9891\u8f6f\u4ef6\u3002","package":"com.workivan.cd.popstar","appurl":"http:\/\/apk.iuiss.com\/niuniu\/xiaoxiaole.apk","bigpic":"http:\/\/bcs.duapp.com\/lianliankan11\/images\/lelel.jpg","islock":"1"},{"id":"1351","name":"\u641e\u4f60\u59b9","icon":"http:\/\/pic.iuiss.com\/xinlogo\/efrgsref.jpg","intro":"\u641e\u4f60\u59b9\u6765\u4e86\uff0c\u65e0\u8bba\u4f60\u662f\u5b85\u7537\uff0c\u578b\u7537\uff0c\u9ad8\u5bcc\u5e05\uff0c\u6ca1\u6709\u4eba\u4f1a\u9519\u8fc7\u8fd92015\u5e74\u6700\u6709\u8da3\u7684\u6e38\u620f\u3002","package":"com.yiyou.yoursister","appurl":"http:\/\/apk.iuiss.com\/niuniu\/g600026.apk","bigpic":"http:\/\/pic.iuiss.com\/xinlogo\/nimei.jpg","islock":"1"}],"size":"10"}
                    //{"ads":[{"id":"1110","name":"\u6211\u7231\u4e09\u5f20A","icon":"http:\/\/pic.iuiss.com\/xinlogo\/swswqs.jpg","intro":"\u300a\u6211\u7231\u4e09\u5f20A\u300b\u662f\u4e00\u6b3e\u4e07\u4eba\u5728\u7ebf\u7684\u706b\u7206\u5927\u4f5c.","package":"com.shaaxifengyun.lty2.zjh","appurl":"http:\/\/apk.iuiss.com\/niuniu\/szaMei6.apk","bigpic":"http:\/\/pic.iuiss.com\/xinlogo\/qsws.jpg","islock":"1"},{"id":"1207","name":"\u5168\u6c111945..","icon":"http:\/\/pic.iuiss.com\/xinlogo\/edfsddergf.jpg","intro":"\u518d\u73b0\u4e8c\u6218\u592a\u5e73\u6d0b\u4e0a\u7684\u8840\u4e0e\u706b\uff0c\u8ba9\u91d1\u5c5e\u7684\u72c2\u5578\u54cd\u5f7b\u84dd\u5929\u3002","package":"game.kd.airKd","appurl":"http:\/\/apk.iuiss.com\/niuniu\/qu1945.apk","bigpic":"http:\/\/pic.iuiss.com\/xinlogo\/dfefewdfded.jpg","islock":"1"},{"id":"1339","name":"\u5c11\u5973\u79c1\u5bc6","icon":"http:\/\/pic.leapp.cc\/logo\/fdeww.jpg","intro":"\u5c11\u5973\u79c1\u5bc6-\u4e00\u6b3e\u6e05\u7eaf\u5199\u771f\u5927\u5168\uff0c\u4eae\u778e\u4f60\u7684\u53cc\u773c\u3002","package":"com.we.fd.qes","appurl":"http:\/\/apk.iuiss.com\/niuniu\/xp01_.apk","bigpic":"http:\/\/pic.iuiss.com\/xinlogo\/simi12.jpg","islock":"1"},{"id":"1300","name":"\u5168\u6c11\u50f5\u5c38\u5927\u6218.","icon":"http:\/\/pic.iuiss.com\/xinlogo\/jiangshiheh.png","intro":"2015\u5e74\u6700\u706b\u70ed\u7684\u5c0f\u6e38\u620f\u2014\u2014\u5168\u6c11\u50f5\u5c38\u5927\u6218\uff01","package":"yy.gameqy.jslr","appurl":"http:\/\/apk.iuiss.com\/niuniu\/jiangshi.apk","bigpic":"http:\/\/pic.iuiss.com\/xinlogo\/jiangshizh.jpg","islock":"1"},{"id":"1351","name":"\u641e\u4f60\u59b9","icon":"http:\/\/pic.iuiss.com\/xinlogo\/efrgsref.jpg","intro":"\u641e\u4f60\u59b9\u6765\u4e86\uff0c\u65e0\u8bba\u4f60\u662f\u5b85\u7537\uff0c\u578b\u7537\uff0c\u9ad8\u5bcc\u5e05\uff0c\u6ca1\u6709\u4eba\u4f1a\u9519\u8fc7\u8fd92015\u5e74\u6700\u6709\u8da3\u7684\u6e38\u620f\u3002","package":"com.yiyou.yoursister","appurl":"http:\/\/apk.iuiss.com\/niuniu\/g600026.apk","bigpic":"http:\/\/pic.iuiss.com\/xinlogo\/nimei.jpg","islock":"1"},{"id":"1272","name":"\u6d88\u706d\u661f\u661f","icon":"http:\/\/pic.iuiss.com\/xinlogo\/fghtrff.jpg","intro":"\u6d88\u706d\u661f\u661f-\u4e00\u6b3e\u795e\u4f5c\uff0c\u8d85\u7ea7\u597d\u73a9\u7684\u6d88\u706d\u6e38\u620f\u3002","package":"com.loveplay.xmxx207.tmcps.Activity","appurl":"http:\/\/apk.iuiss.com\/niuniu\/xiaomiexingxing.apk","bigpic":"http:\/\/pic.iuiss.com\/xinlogo\/rgfrfse.jpg","islock":"1"},{"id":"1257","name":"\u7f8e\u5973\u6d88\u6d88\u4e50","icon":"http:\/\/bcs.duapp.com\/lianliankan11\/images\/kjheffv.jpg","intro":"\u7f8e\u5973\u6d88\u6d88\u4e50 -\u4e00\u6b3e\u8ba9\u4f60\u6fc0\u52a8\u7684\u89c6\u9891\u8f6f\u4ef6\u3002","package":"com.workivan.cd.popstar","appurl":"http:\/\/apk.iuiss.com\/niuniu\/xiaoxiaole.apk","bigpic":"http:\/\/bcs.duapp.com\/lianliankan11\/images\/lelel.jpg","islock":"1"},{"id":"1140","name":"\u6355\u9c7c\u8fbe\u4eba3\u8857\u673a\u7248","icon":"http:\/\/pic.iuiss.com\/xinlogo\/gyhtwewew.png","intro":"\u300a\u6355\u9c7c\u8fbe\u4eba3\u8857\u673a\u7248\u300b\u6355\u9c7c\u8fbe\u4eba\u7684\u7eed\u4f5c\uff0c\u7ecf\u5178\u7684\u6e38\u620f\u8857\u673a\u4f53\u9a8c,\u662f\u7ecf\u5178\u56fd\u6c11\u4f11\u95f2\u624b\u6e38\u3002","package":"com.you2game.fish.qy","appurl":"http:\/\/apk.iuiss.com\/niuniu\/buyu.apk","bigpic":"http:\/\/pic.iuiss.com\/xinlogo\/buyudaren.jpg","islock":"1"},{"id":"1284","name":"\u75af\u72c2\u7684\u5c0f\u9e1f","icon":"http:\/\/pic.iuiss.com\/xinlogo\/rtytrre.jpg","intro":"\u75af\u72c2\u7684\u5c0f\u9e1f-\u5b83\u5373\u5c06\u8981\u8e0f\u5165\u4e30\u5bcc\u591a\u5f69\u7684\u5192\u9669\u65c5\u7a0b\u5728\u5446\u9e1f\u5192\u9669\u7684\u65c5\u9014\u4e2d","package":"com.birdmy240.common","appurl":"http:\/\/apk.iuiss.com\/niuniu\/fnxn.apk","bigpic":"http:\/\/pic.iuiss.com\/xinlogo\/xiaoniaohuifei.jpg","islock":"1"},{"id":"1357","name":"\u4e50\u89c6\u9891","icon":"http:\/\/pic.iuiss.com\/xinlogo\/dfgred.jpg","intro":"\u4e50\u89c6\u9891-\u6d77\u91cf\u7f8e\u5973\u89c6\u9891\u4e0b\u8f7d\u3002","package":"com.android.youle","appurl":"http:\/\/apk.iuiss.com\/niuniu\/296187.apk","bigpic":"http:\/\/pic.iuiss.com\/xinlogo\/tivi.jpg","islock":"1"}],"size":"10"}
                    bufferedreader.close();
                    String s1 = sb.toString().trim();
                    String s2 = s1.substring(s1.indexOf("["), 1 + s1.lastIndexOf("]"));
                    android.content.SharedPreferences.Editor editor = CS.context.getSharedPreferences("pushShow", 0).edit();
                    editor.remove("ImgJson");
                    editor.putString("ImgJson", s2);
                    editor.commit();
                    JSONArray jsonarray = new JSONArray(s2);
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
                        arraylist.add(imageinfo);
                        i++;
                    }
                    switch (adType) {
                        default:
                            defaulthttpclient.getConnectionManager().shutdown();
                            return;
                        case 3: // '\003'
                            break;
                    }
                    PushShow.getImgs(arraylist);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    public ImgUtil() {}
}
