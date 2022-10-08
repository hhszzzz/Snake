package Client.service;

import comment.Message;
import comment.MessageType;
import comment.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @author hhs
 * @version 1.0
 * 该类完成用户登录验证和注册等功能
 */
public class UserClientService {

    // 因为可能在其他地方使用 user 信息，因此做成成员属性
    private User user = new User();
    // Socket在其他地方也可能你使用
    private Socket socket;

    // 根据 userId 和 pwd 到服务器验证该用户是否合法
    public boolean checkUser(String userId, String pwd) {
        boolean b = false;
        // 创建User对象
        user.setUserId(userId);
        user.setPassword(pwd);

        try {
            // 连接到服务端，发送u对象
            socket = new Socket(InetAddress.getLocalHost(), 9999);
            // 得到 ObjectOutputStream 对象
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(user);

            //读取从服务器回复的Message对象
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Message ms = (Message) ois.readObject();

            if (ms.getMesType().equals(MessageType.MESSAGE_LOGIN_SUCCEED)) { // 登录成功
                // 创建一个和服务器端保持通信的线程

                ClientConnectServerThread ccst = new ClientConnectServerThread(socket);
                //启动客户端的线程
                ccst.start();
                // 为了后期客户端的扩展，将线程放到集合中进行管理
                ManageClientConnectServerThread.addClientConnectServerThread(userId, ccst);
                b = true;
            } else { // 登录失败
                // 不能启动和服务器通信的线程，关闭Socket
                socket.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return b;
    }


    // 退出客户端，给服务端发送退出系统的 message
    public void logout() { // 注销/退出
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_CLIENT_EXIT);
        message.setSender(user.getUserId()); // 指定是哪个用户

        // 发送 message
        try {
//            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectOutputStream oos = new ObjectOutputStream(
                    ManageClientConnectServerThread.getClientConnectServerThread(user.getUserId()).getSocket().getOutputStream()
            );
            oos.writeObject(message);
            System.out.println(user.getUserId() + " 退出了系统");
            System.exit(0); // 结束进程
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
