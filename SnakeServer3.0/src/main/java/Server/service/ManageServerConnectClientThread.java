package Server.service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hhs
 * @version 1.0
 * 管理和客户端通信的线程
 */

public class ManageServerConnectClientThread {

    private static ConcurrentHashMap<String, ServerConnectClientThread> hm = new ConcurrentHashMap<>();

    // 添加线程
    public static void addServerConnectClientThread(String userId, ServerConnectClientThread scct) {
        hm.put(userId, scct);
    }

    // 得到线程
    public static ServerConnectClientThread getServerConnectThread(String userId) { return hm.get(userId); }

    // 移除一个用户的线程
    public static void removeServerConnectClientThread(String userId) {
        hm.remove(userId);
    }

    //返回 hm
    public static ConcurrentHashMap<String, ServerConnectClientThread> getHm() {
        return hm;
    }

}
