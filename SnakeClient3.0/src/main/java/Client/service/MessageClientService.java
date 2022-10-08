package Client.service;

import comment.ManageContent;
import comment.Message;
import comment.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * @author hhs
 * @version 1.0
 * 该类，提供和消息相关的方法
 */
public class MessageClientService {

    /**
     * solo
     * @param receiver 接收者
     * @param senderId 发送者
     */
    public void sendMessageToOne(String receiver, String senderId) {
        // 构建 message
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_REQUEST_SOLO); // solo
        message.setSender(senderId); // 发送者
        message.setGetter(receiver); // 接收者
        message.setContent(ManageContent.getContent(senderId)); // 消息内容

        // 发送给服务端
        try {
            ObjectOutputStream oos = new ObjectOutputStream(
                    ManageClientConnectServerThread.getClientConnectServerThread(senderId).getSocket().getOutputStream()
            );
            // 发送！
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 群发（几乎与私聊一致）
     * @param senderId 发送者
     */
    public void sendMessageToAll(String senderId) {
        // 构建 message
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_TO_ALL_MES); // 设置普通消息包
        message.setSender(senderId); // 发送者
        message.setContent(ManageContent.getContent(senderId)); // 消息内容


        // 发送给服务端
        try {
            ObjectOutputStream oos = new ObjectOutputStream(
                    ManageClientConnectServerThread.getClientConnectServerThread(senderId).getSocket().getOutputStream()
            );
            // 发送！
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
