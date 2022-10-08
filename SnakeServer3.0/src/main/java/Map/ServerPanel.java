package Map;

import Block.Node;
import Foods.NormalFood;
import Foods.SuperFood;
import Obstacle.Obstacle;
import Server.service.SnakeServer;
import Snakes.P1;
import comment.Content;
import comment.ManageContent;
import comment.User;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class ServerPanel implements Runnable{
    public Vector<P1> p1s = null;
    public SuperFood superFood = null;
    public HashSet<Node> overlap = new HashSet<>();
    public HashSet<NormalFood> normalFoods = new HashSet<>();

    public ServerPanel() { // 所有元素的初始化
        // 防止障碍物互相重复
        for (int k = 0; k < 6; k ++ ) {
            Obstacle ob = new Obstacle();
            // 对障碍物的每个节点遍历，查找是否存在重复的点
            int idx = -1;
            do {
                if (idx != -1) {
                    ob.init(); // 让障碍物再重新生成一次
                    idx = -1;
                }
                for (int i = 0; i < ob.nodes.size(); i++) {
                    if (overlap.contains(ob.nodes.get(i))) { // 找到重复的点（注意！！！！！！！！！这里的点是 / 20 的）
                        idx = i;
                        break;
                    }
                }
            } while (idx != -1);
            // 将障碍物储存进去
            overlap.addAll(ob.nodes);
        }

        // 生成普通食物（自定义数量）
        for (int i = 0; i < 6; i++) normalFoods.add(new NormalFood());
        // 生成特殊食物a
        superFood = new SuperFood();


        // 添加用户
        ConcurrentHashMap<String, User> validUsers = SnakeServer.getValidUsers();
        p1s = new Vector<>(validUsers.size());
        // 遍历所有合法用户，查找该用户是否上线，如果上线，就添加到 p1数组中
        Iterator<String> iterator = validUsers.keySet().iterator();
        while (iterator.hasNext()) {
            String userId = iterator.next();
            p1s.add(new P1(overlap, userId));
        }
    }

    @Override
    public void run() {

        while (true) {
            // 休眠125ms
            try {
                Thread.sleep(125);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            // 防止普通食物重叠
            for (NormalFood normalFood : normalFoods)
                while (overlap.contains((Node) normalFood)) normalFood.init();
            // 防止特殊食物重叠
            while (overlap.contains((Node) superFood)) superFood.init();

            // 服务端传送的信息 - 内容
            Content content = new Content();
            content.setSnake(p1s);
            content.setObstacles(overlap);
            content.setNormalFoods(normalFoods);
            content.setSuperFood(superFood);
            ManageContent.addContent("服务器", content);
        }
    }
}