package Client.service;

import Client.service.utils.Utility;
import Map.Map;
import comment.ManageContent;
import comment.Message;
import comment.MessageType;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author hhs
 * @version 1.0
 * 客户端连接服务端的线程
 */
public class ClientConnectServerThread extends Thread {
    // 该线程需要持有 Socket
    private Socket socket;
    // 持有 content

    // 接收一个socket对象
    public ClientConnectServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        // 因为Thread需要在后台和服务器通信，使用while 循环
        while (true) {

            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                // 如果服务器没有发送 Message对象，线程会阻塞在此
                Message message = (Message) ois.readObject();

                // 判断这个 message 类型，做相应的业务处理
                if (message.getMesType().equals(MessageType.MESSAGE_REQUEST_SOLO)) { // 请求solo

                    // 请求进行连接

                    //需要发送一个信息，表明是否同意solo
                    Message message1 = new Message();
                    // 接收者变成了发送者，发送者变成接收者
                    message1.setSender(message.getGetter());
                    message1.setGetter(message.getSender());

                    // 如果同意，就开启游戏界面
                    new Thread(new Map()).start();
                    // 并且开启连接的线程
                    new Thread(new ClientConnectingClient(message.getGetter(), message.getSender())).start();
                    // 同意
                    message1.setMesType(MessageType.MESSAGE_AGREE_SOLO);

                    // 发送消息
                    ObjectOutputStream oos = new ObjectOutputStream(
                            ManageClientConnectServerThread.getClientConnectServerThread(message1.getSender()).getSocket().getOutputStream()
                    );
                    oos.writeObject(message1);

                } else if (message.getMesType().equals(MessageType.MESSAGE_AGREE_SOLO)) { // 同意solo
                    System.out.println("客户端 " + message.getSender() + " 同意了你的solo请求");
                    // 如果客户端同意solo，就开启游戏界面
                    new Thread(new Map()).start();
                    // 并且开启连接的线程
                    new Thread(new ClientConnectingClient(message.getGetter(), message.getSender())).start();

                } else if (message.getMesType().equals(MessageType.MESSAGE_REFUSE_SOLO)) { // 拒绝solo
                    // 如果拒绝solo
                    System.out.println("客户端 " + message.getSender() + " 拒绝了你的solo请求");

                } else if (message.getMesType().equals(MessageType.MESSAGE_CONNECTING)) { // 连接中

                    ManageContent.addContent(message.getGetter(), message.getContent());

                } else if (message.getMesType().equals(MessageType.MESSAGE_TO_ALL_MES)) { // 群发
                    System.out.println("收到 " + message.getSender() + " 的群发");
                    // sender - 服务器
                    ManageContent.addContent(message.getGetter(), message.getContent());

                } else {
                    System.out.println("其他类型的message，暂不处理");
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Socket getSocket() {
        return socket;
    }
}
