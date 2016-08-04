package com.bbpay.android.utils;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import android.content.Context;
import android.os.AsyncTask;
import com.bbpay.android.listener.RequestCallback;

public class RequestUtils {
    private static void doGet(final Context context, final String url, final RequestCallback callback, final int tryCount) {
        try {
            MyLogger.info("RequestUtils.get:url=" + url);
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode >= 400) { throw new RuntimeException("statusCode=" + statusCode); }
            String result = EntityUtils.toString(httpResponse.getEntity(), UTF_8);
            MyLogger.info("RequestUtils.get:result=" + result);
            if (callback != null) callback.onResult(result, null);
        } catch (Throwable e) {
            MyLogger.error(e);
            if (tryCount > 1) {
                doGet(context, url, callback, tryCount - 1);
            } else {
                if (callback != null) callback.onResult(null, e);
            }
        }
    }
    private static void doEncryptPost(final Context context, final String url, final JSONObject obj, final int tryCount, final RequestCallback callback) {
        try {
            MyLogger.info("RequestUtils.jsonPost:\nurl=" + url + "\ndata=" + obj.toString());
            String current = String.valueOf(System.currentTimeMillis());
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost post = new HttpPost(url);
            post.addHeader(InfoUtils.TIME, current);
            List<NameValuePair> paramList = new ArrayList<NameValuePair>();
            final String password = InfoUtils.generatePassword(current);
            BasicNameValuePair param = new BasicNameValuePair("d", AES.encode(obj.toString(), password));
            paramList.add(param);
            post.setEntity(new UrlEncodedFormEntity(paramList, UTF_8));
            HttpResponse httpResponse = httpClient.execute(post);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode >= 400) { throw new RuntimeException("statusCode=" + statusCode); }
            String result = AES.decode(EntityUtils.toString(httpResponse.getEntity(), UTF_8), password);
            MyLogger.info("RequestUtils.jsonPost:result=" + result);
            if (callback != null) callback.onResult(result, null);
        } catch (Throwable e) {
            MyLogger.error(e);
            if (tryCount > 1) {
                doEncryptPost(context, url, obj, tryCount - 1, callback);
            } else {
                if (callback != null) callback.onResult(null, e);
            }
        }
    }
    public static void get(final Context context, final String url, final RequestCallback callback, final int tryCount) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... p) {
                doGet(context, url, callback, tryCount);
                return null;
            }
        }.execute(new Void[0]);
    }
    public static void encryptPost(final Context context, final String url, final JSONObject obj) {
        encryptPost(context, url, obj, null);
    }
    public static void encryptPost(final Context context, final String url, final JSONObject obj, final RequestCallback callback) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... p) {
                doEncryptPost(context, url, obj, tryCount, callback);
                return null;
            }
        }.execute(new Void[0]);
    }
    private static final String UTF_8 = "UTF-8";
    private static final int tryCount = 1;
}
