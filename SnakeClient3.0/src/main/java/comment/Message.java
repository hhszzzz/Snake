package comment;

import java.io.Serializable;

/**
 * @author hhs
 * @version 1.0
 * 表示客户端和服务器端通信时的消息对象
 */
public class Message implements Serializable {
    private static final long serialVersionUID = 380602367336388602L;
    private String sender;//发送者
    private String getter;//接收者
    private Content content;//消息内容
//    private String sendTime;//发送时间
    private String mesType;//消息类型（可以在接口定义消息类型）

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getGetter() {
        return getter;
    }

    public void setGetter(String getter) {
        this.getter = getter;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public String getMesType() {
        return mesType;
    }

    public void setMesType(String mesType) {
        this.mesType = mesType;
    }
}
