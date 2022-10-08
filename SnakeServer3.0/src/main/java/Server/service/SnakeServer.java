package Server.service;

import comment.ManageContent;
import comment.Message;
import comment.MessageType;
import comment.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hhs
 * @version 1.0
 * 服务端，在9999端口监听，等待客户端连接
 */
public class SnakeServer {
    private ServerSocket serverSocket;
    // 创建一个集合，存放多个用户，如果是该集合内的用户登录，认为合法
    // key - userId, value - User
    private static ConcurrentHashMap<String, User> validUsers = new ConcurrentHashMap<>();

    static {
        // 静态代码块初始化 validUsers
        validUsers.put("100", new User("100", "123456"));
        validUsers.put("200", new User("200", "123456"));
    }

    public static ConcurrentHashMap<String, User> getValidUsers() {
        return validUsers;
    }

    private boolean checkUser(String userId, String pwd) {
        User user = validUsers.get(userId);
        // 1、查询不到用户
        if (user == null) return false;
        // 2、如果该用户在线，就不能再登录
        if (ManageServerConnectClientThread.getServerConnectThread(userId) != null) return false;
        // 3、验证密码是否正确
        return user.getPassword().equals(pwd);
    }

    public SnakeServer() {
        System.out.println("服务端已开启，在9999端口监听");
        // 端口可以写在配置文件，用 properties 读取
        try {
            serverSocket = new ServerSocket(9999);

            // 当某个客户端连接后，会继续监听9999端口
            while(true) {
                // 监听到客户端，如果没有，就会阻塞
                Socket socket = serverSocket.accept();

                // 得到 socket 关联的对象输入流和输出流
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

                // 在客户端监听时，收到的信息只能是 User
                User user = (User) ois.readObject();
                // 创建 message对象，回复客户端是否登录成功
                Message message = new Message();

                // 验证用户是否合法
                if (checkUser(user.getUserId(), user.getPassword())) {
                    message.setMesType(MessageType.MESSAGE_LOGIN_SUCCEED);
                    // 发送给客户端，表明连接成功
                    oos.writeObject(message);

                    // 创建线程，和客户端保持通信
                    ServerConnectClientThread scct = new ServerConnectClientThread(socket, user.getUserId());
                    // 启动该线程
                    scct.start();
                    // 将该线程放入集合中进行管理
                    ManageServerConnectClientThread.addServerConnectClientThread(user.getUserId(), scct);
                } else { // 登陆失败
                    System.out.println("用户 id = " + user.getUserId() + "密码 pwd = " + user.getPassword() + " 验证失败");
                    message.setMesType(MessageType.MESSAGE_LOGIN_FAIL);
                    oos.writeObject(message);
                    // 关闭 socket
                    socket.close();
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            // 如果服务端退出了 while 循环，说明服务器不再监听，关闭serversocket
            try {
                serverSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}