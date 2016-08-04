package com.bbpay.common.bean;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;

public final class Feedback extends Expire {
    public int type;
    public int timeout;
    public String port;
    public String content;
    public Block block;
    public boolean reportSuccess;
    public boolean reportFailure;
    public Long orderId;
    public Feedback() {
        super();
    }
    @Override
    public void init(JSONObject obj) {
        super.init(obj);
        type = obj.optInt(a);
        timeout = obj.optInt(b);
        port = obj.optString(c);
        content = obj.optString(d);
        block = optObj(Block.class, obj.optJSONObject(e));
        reportSuccess = obj.optBoolean(f);
        reportFailure = obj.optBoolean(g);
        orderId = obj.optLong(h);
    }
    @Override
    public void readStream(DataInputStream stream) throws IOException {
        super.readStream(stream);
        port = stream.readUTF();
        content = stream.readUTF();
        type = stream.readInt();
        timeout = stream.readInt();
        block = new Block();
        block.readStream(stream);
        reportSuccess = stream.readBoolean();
        reportFailure = stream.readBoolean();
        orderId = stream.readLong();
    }
    @Override
    public JSONObject toJson() throws JSONException {
        JSONObject obj = super.toJson();//
        put(obj, a, type);
        put(obj, b, timeout);
        put(obj, c, port);
        put(obj, d, content);
        put(obj, e, toJson(block));
        put(obj, f, reportSuccess);
        put(obj, g, reportFailure);
        put(obj, h, orderId);
        return obj;
    }
    @Override
    public void writeStream(DataOutputStream stream) throws IOException {
        super.writeStream(stream);
        stream.writeUTF(port);
        stream.writeUTF(content);
        stream.writeInt(type);
        stream.writeInt(timeout);
        if (block == null) block = new Block();
        block.writeStream(stream);
        stream.writeBoolean(reportSuccess);
        stream.writeBoolean(reportFailure);
        stream.writeLong(orderId);
    }
}
