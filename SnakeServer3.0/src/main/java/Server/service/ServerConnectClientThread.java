package Server.service;

import comment.ManageContent;
import comment.Message;
import comment.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hhs
 * @version 1.0
 * 该类的对象和某个客户端保持通信，将 socket和 userId绑定
 * 一个类对应唯一的一个客户端
 */
public class ServerConnectClientThread extends Thread {
    private Socket socket;
    private String userId; // 连接到服务端的用户Id

    public ServerConnectClientThread(Socket socket, String userId) {
        this.socket = socket;
        this.userId = userId;
    }

    public Socket getSocket() {
        return socket;
    }

    public void sendMessageToAllService(String userId) {
        System.out.println("服务器开启推送服务！" + userId);
        // 构建 message
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_TO_ALL_MES);
        message.setSender("服务器");
        message.setGetter(userId);
        // 构建 content 并传入 message
        message.setContent(ManageContent.getContent("服务器"));
        // 发送 message
        try {
            ObjectOutputStream oos = new ObjectOutputStream(
                    ManageServerConnectClientThread.getServerConnectThread(userId).getSocket().getOutputStream()
            );
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 线程处于run状态，可以发送/接收信息
     * 目前的能接收的信息有：
     * 1、普通信息(solo)
     * 2、群发信息(多人)
     * 3、客户端请求退出
     */
    @Override
    public void run() {
        // 先给客户端发送一份地图布局，即食物和障碍物
        sendMessageToAllService(userId);
        System.out.println("客户端" + userId + " 与服务端保持通信");

        while (true) {

            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                // 从客户端读取到的 message
                Message message = (Message) ois.readObject();

                // 根据 message 的类型，做相应业务处理
                if (message.getMesType().equals(MessageType.MESSAGE_REQUEST_SOLO)) { // 请求solo
                    System.out.println(message.getSender() + " 请求与 " + message.getGetter() + " solo");

                    // 发送给指定对象
                    ObjectOutputStream oos = new ObjectOutputStream(
                            ManageServerConnectClientThread.getServerConnectThread(message.getGetter()).getSocket().getOutputStream()
                    );
                    oos.writeObject(message);
                } else if (message.getMesType().equals(MessageType.MESSAGE_AGREE_SOLO) ||
                            message.getMesType().equals(MessageType.MESSAGE_REFUSE_SOLO)) {
                    // 发送给指定对象
                    ObjectOutputStream oos = new ObjectOutputStream(
                            ManageServerConnectClientThread.getServerConnectThread(message.getGetter()).getSocket().getOutputStream()
                    );
                    oos.writeObject(message);
                } else if (message.getMesType().equals(MessageType.MESSAGE_CONNECTING)) {
                    // 一直建立连接，即对服务器的content进行修改
                    // 修改发送者
                    message.setSender("服务器");
                    // 发送给指定对象
                    ObjectOutputStream oos = new ObjectOutputStream(
                            ManageServerConnectClientThread.getServerConnectThread(message.getGetter()).getSocket().getOutputStream()
                    );
                    oos.writeObject(message);
                } else if (message.getMesType().equals(MessageType.MESSAGE_TO_ALL_MES)) { // 更多人

                    // 遍历线程池，除自己以外的所有线程的 socket，转发 message
                    ConcurrentHashMap<String, ServerConnectClientThread> hm = ManageServerConnectClientThread.getHm();

                    Iterator<String> iterator = hm.keySet().iterator();
                    while (iterator.hasNext()) {
                        String onlineUserId = iterator.next();
                        if (!onlineUserId.equals(message.getSender())) { // 排除自己
                            // 转发
                            ObjectOutputStream oos = new ObjectOutputStream(hm.get(onlineUserId).getSocket().getOutputStream());
                            oos.writeObject(message);
                        }
                    }
                } else if (message.getMesType().equals(MessageType.MESSAGE_CLIENT_EXIT)) { // 客户端请求退出
                    // 客户端退出
                    System.out.println(message.getSender() + " 请求退出");
                    // 将客户端对应的线程从集合中删除
                    ManageServerConnectClientThread.removeServerConnectClientThread(message.getSender());
                    // 关闭发送者的连接
                    socket.close();
                    // 退出该发送者的线程
                    break;
                } else {
                    System.out.println("其他类型的message，暂不做处理");
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
