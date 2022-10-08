package Client.service;

import comment.ManageContent;
import comment.Message;
import comment.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * @author hhs
 * @version 1.0
 * 此类用于不停的对服务端发送信息，来修改服务端的message中的content
 */
public class ClientConnectingClient implements Runnable {

    private String senderId;
    private String getterId;

    public ClientConnectingClient(String senderId, String getterId) {
        this.senderId = senderId;
        this.getterId = getterId;
    }

    @Override
    public void run() {
        System.out.println("Connecting...");
        while (true) {

            try {
                Thread.sleep(125);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            Message message = new Message();
            message.setMesType(MessageType.MESSAGE_CONNECTING);

            message.setSender(senderId);
            message.setGetter(getterId);
            // 将我方信息写入 message 中
            message.setContent(ManageContent.getContent(senderId));

            // 将信息发送给客户端
            try {
                ObjectOutputStream oos = new ObjectOutputStream(
                        ManageClientConnectServerThread.getClientConnectServerThread(senderId).getSocket().getOutputStream()
                );
                oos.writeObject(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
