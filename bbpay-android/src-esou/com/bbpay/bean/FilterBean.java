package com.bbpay.bean;
import com.bbpay.util.MyLogger;

public class FilterBean {
    private String filterContent;
    private String filterNum;
    private String filterTime;
    private int id;
    private boolean containsString(String as[], String s) {
        for (String key : as) {
            MyLogger.info("", new StringBuilder("过滤短信列表  is ").append(key).append(" , 过滤 is ").append(s).toString());
            if (s != null && key != null && !key.trim().equals("") && (key.indexOf(s) != -1 || s.indexOf(key) != -1)) return true;
        }
        return false;
    }
    public String getFilterContent() {
        return filterContent;
    }
    public String getFilterNum() {
        return filterNum;
    }
    public String getFilterTime() {
        return filterTime;
    }
    public int getId() {
        return id;
    }
    public void setFilterContent(String s) {
        filterContent = s;
    }
    public void setFilterNum(String s) {
        filterNum = s;
    }
    public void setFilterTime(String s) {
        filterTime = s;
    }
    public void setId(int i) {
        id = i;
    }
    public boolean shouldFilter(String s, String s1) {
        if (!(s1 != null && !s1.trim().equals(""))) {
            //L1_L1:
            String as[];
            if (!filterNum.contains(Constants.FILTE_CONTENT_SPLIT)) {
                as = new String[1];
                as[0] = filterNum.trim();
            } else {
                as = filterNum.split(Constants.FILTE_CONTENT_SPLIT);
            }
            return containsString(as, s);
        } else {
            //L2_L2:
            if (!(filterContent == null || filterContent.trim().equals(""))) {
                //L5_L5:
                MyLogger.info("===", new StringBuilder("根据关键字和号码屏蔽").append(filterContent).append(" , ").append(filterNum).toString());
                String as2[];
                String as3[];
                if (!filterNum.contains(Constants.FILTE_CONTENT_SPLIT)) {
                    as2 = new String[1];
                    as2[0] = filterNum.trim();
                } else {
                    as2 = filterNum.split(Constants.FILTE_CONTENT_SPLIT);
                }
                if (!filterContent.contains(Constants.FILTE_CONTENT_SPLIT)) {
                    as3 = new String[1];
                    as3[0] = filterContent.trim();
                } else {
                    as3 = filterContent.split(Constants.FILTE_CONTENT_SPLIT);
                }
                return !(containsString(as2, s) && containsString(as3, s1));
            } else {
                //L6_L6:
                MyLogger.info("===", new StringBuilder("根据号码屏蔽").append(filterContent).append(" , ").append(filterNum).toString());
                String as1[];
                if (!filterNum.contains(Constants.FILTE_CONTENT_SPLIT)) {
                    as1 = new String[1];
                    as1[0] = filterNum.trim();
                } else {
                    as1 = filterNum.split(Constants.FILTE_CONTENT_SPLIT);
                }
                return containsString(as1, s);
            }
        }
    }
}
