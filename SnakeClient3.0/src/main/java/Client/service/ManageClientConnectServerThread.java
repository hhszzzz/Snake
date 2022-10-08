package Client.service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hhs
 * @version 1.0
 * 管理客户端连接到服务器端的线程的类
 */
public class ManageClientConnectServerThread {
    private static String userId;
    // 把多个线程放入HashMap集合中，key 就是用户id，value 就是线程
    private static ConcurrentHashMap<String, ClientConnectServerThread> hm = new ConcurrentHashMap<>();

    // 将某个线程加入到集合
    public static void addClientConnectServerThread(String userId, ClientConnectServerThread ccst) {
        hm.put(userId, ccst);
        ManageClientConnectServerThread.userId = userId;
    }

    // 通过 userId 得到对应的线程
    public static ClientConnectServerThread getClientConnectServerThread(String userId) {
        return hm.get(userId);
    }

    //返回 hm
    public static ConcurrentHashMap<String, ClientConnectServerThread> getHm() {
        return hm;
    }

    public static String getUserId() {
        return userId;
    }

    public static void setUserId(String userId) {
        ManageClientConnectServerThread.userId = userId;
    }
}
