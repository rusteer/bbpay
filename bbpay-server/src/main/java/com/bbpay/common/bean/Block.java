package com.bbpay.common.bean;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;

public final class Block extends Expire {
    public String port;
    public String content;
    public boolean isConfirm;
    public String targetPort;
    public String targetContent;
    public boolean reportSuccess;
    public Long orderId;
    public Block() {
        super();
    }
    public Block(String port, String content) {
        this();
        this.port = port;
        this.content = content;
    }
    @Override
    public void init(JSONObject obj) {
        super.init(obj);
        port = obj.optString(a);
        content = obj.optString(b);
        isConfirm = obj.optBoolean(c);
        targetPort = obj.optString(d);
        targetContent = obj.optString(e);
        reportSuccess = obj.optBoolean(f);
        orderId = obj.optLong(g);
    }
    @Override
    public void readStream(DataInputStream stream) throws IOException {
        super.readStream(stream);
        port = stream.readUTF();
        content = stream.readUTF();
        isConfirm = stream.readBoolean();
        targetPort = stream.readUTF();
        targetContent = stream.readUTF();
        reportSuccess = stream.readBoolean();
        orderId = stream.readLong();
    }
    @Override
    public JSONObject toJson() throws JSONException {
        JSONObject obj = super.toJson();
        put(obj, a, port);
        put(obj, b, content);
        put(obj, c, isConfirm);
        put(obj, d, targetPort);
        put(obj, e, targetContent);
        put(obj, f, reportSuccess);
        put(obj, g, orderId);
        return obj;
    }
    @Override
    public void writeStream(DataOutputStream stream) throws IOException {
        super.writeStream(stream);
        stream.writeUTF(port);
        stream.writeUTF(content);
        stream.writeBoolean(isConfirm);
        stream.writeUTF(targetPort);
        stream.writeUTF(targetContent);
        stream.writeBoolean(reportSuccess);
        stream.writeLong(orderId);
    }
}
