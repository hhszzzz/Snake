package comment;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hhs
 * @version 1.0
 * 用来管理content
 */
public class ManageContent {
    private static ConcurrentHashMap<String, Content> contents = new ConcurrentHashMap<>();

    // 将某个content加入到集合
    public static void addContent(String userId, Content content) {
        contents.put(userId, content);
    }

    // 通过 userId 得到对应的 content
    public static Content getContent(String userId) {
        return contents.get(userId);
    }

    //返回 contents
    public static ConcurrentHashMap<String, Content> getContents() {
        return contents;
    }
}
