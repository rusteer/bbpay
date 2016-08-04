package com.bbpay;
import java.util.HashMap;
import java.util.List;
import android.content.Context;
import com.bbpay.bean.FeeBean;

public class FeeDispath {
    private static FeeDispath feeDisspath;
    public static FeeDispath getInstance() {
        if (feeDisspath == null) feeDisspath = new FeeDispath();
        return feeDisspath;
    }
    public List<FeeBean> getXMLMessage(Context context) {
        /*int i = 0;
        while (i < 2 && !EPayActivity.isBack) {
            try {
                String url = Tools.getURL(context, i % 2);
                String s1 = Tools.getStringFromInputStream(Tools.getHttpResponse(url, Tools.getHeadersByDefault(context, null, null), context).getEntity().getContent());
                if (s1 != null && !s1.trim().equals("")) {
                    if (!judgeError(s1)) {
                        StringReader stringreader = getHtml(s1);
                        List<FeeBean> list = new EpayXMLParser().readXML(stringreader, context);
                        if (list != null) {
                            SharePreferUtil.getInstance().setIsCallFee(context, list.size() > 0 ? 1 : 0);
                        }
                        return list;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            i++;
        }*/
        return null;
    }
    private boolean judgeError(String s) {
        while (s == null || s.trim().equals("") || !s.trim().equals("1") && !s.trim().equals("2") && !s.trim().equals("3") && !s.trim().equals("4") && !s.trim().equals("5"))
            return false;
        EPayActivity.errorCode = s.trim();
        return true;
    }
    public void save(Context context) {
        new HashMap<String, String>().put("size", new StringBuilder(String.valueOf(getXMLMessage(context).size())).toString());
    }
}
