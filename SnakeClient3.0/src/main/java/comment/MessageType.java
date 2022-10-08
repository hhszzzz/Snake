package comment;

/**
 * @author hhs
 * @version 1.0
 * 表示消息类型
 */
public interface MessageType {
    // 在接口中定义了一些常量
    // 不同的值对应不同的消息类型
    String MESSAGE_LOGIN_SUCCEED = "1"; //表示登录成功
    String MESSAGE_LOGIN_FAIL = "2"; // 表示登陆失败
    String MESSAGE_REQUEST_SOLO = "3"; // 客户端发起请求与另一个客户端solo
    String MESSAGE_CLIENT_EXIT = "4"; // 客户端请求退出
    String MESSAGE_TO_ALL_MES = "5"; // 群发消息（只给服务器使用）
    String MESSAGE_AGREE_SOLO = "6"; // 客户端同意进行solo
    String MESSAGE_REFUSE_SOLO = "7"; // 客户端拒绝solo
    // 客户端一直与客户端进行连接，不停的发送画板的信息，而非人为的控制
    String MESSAGE_CONNECTING = "8";
}
