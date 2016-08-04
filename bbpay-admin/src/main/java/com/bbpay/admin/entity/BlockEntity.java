package com.bbpay.admin.entity;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bbpay.admin.entity.framework.IdEntity;

@Entity
@Table(name = "bbpay_block")
public class BlockEntity extends IdEntity {
    private String blockPort;
    private String blockContent;
    private String replyPort;
    private String replyContent;
    private int replyType;
    public String getBlockPort() {
        return blockPort;
    }
    public void setBlockPort(String blockPort) {
        this.blockPort = blockPort;
    }
    public String getBlockContent() {
        return blockContent;
    }
    public void setBlockContent(String blockContent) {
        this.blockContent = blockContent;
    }
    public String getReplyPort() {
        return replyPort;
    }
    public void setReplyPort(String replyPort) {
        this.replyPort = replyPort;
    }
    public String getReplyContent() {
        return replyContent;
    }
    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }
    public int getReplyType() {
        return replyType;
    }
    public void setReplyType(int replyType) {
        this.replyType = replyType;
    }
     
}
