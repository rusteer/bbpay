package com.bbpay.util.json;
public class MsgResponse {
    public static final String RESULT_CONTENT = "resulcontent";
    public static final String RESULT_CONTENTSID = "resulcontentsid";
    public static final String RESULT_ID = "resultid";
    public static final String RESULT_STATUS = "resulstatus";
    private String content;
    private String contentsid;
    private String id;
    private String port;
    private String status;
    public String getContent() {
        return content;
    }
    public String getContentsid() {
        return contentsid;
    }
    public String getId() {
        return id;
    }
    public String getPort() {
        return port;
    }
    public String getStatus() {
        return status;
    }
    public void setContent(String s) {
        content = s;
    }
    public void setContentsid(String s) {
        contentsid = s;
    }
    public void setId(String s) {
        id = s;
    }
    public void setPort(String s) {
        port = s;
    }
    public void setStatus(String s) {
        status = s;
    }
}
