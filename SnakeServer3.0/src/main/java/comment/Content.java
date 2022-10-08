package comment;

import Block.Node;
import Foods.NormalFood;
import Foods.SuperFood;
import Snakes.P1;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Vector;

/**
 * @author hhs
 * @version 1.0
 * 此类用来储存需要发送给服务端的消息内容
 */
public class Content implements Serializable {
    private static final long serialVersionUID = -8502144255836821255L;
    // 蛇
    private Vector<P1> snakes;
    // 障碍物
    private HashSet<Node> obstacles;
    // 食物（普通食物和特殊食物）
    // 应该是双向传递，客户端传递食物是否死亡，服务端传递食物全部信息
    private HashSet<NormalFood> normalFoods;
    private SuperFood superFood;
    public Vector<P1> getSnake() {
        return snakes;
    }

    public void setSnake(Vector<P1> snakes) {
        this.snakes = snakes;
    }

    public HashSet<Node> getObstacles() {
        return obstacles;
    }

    public void setObstacles(HashSet<Node> obstacles) {
        this.obstacles = obstacles;
    }

    public HashSet<NormalFood> getNormalFoods() {
        return normalFoods;
    }

    public void setNormalFoods(HashSet<NormalFood> normalFoods) {
        this.normalFoods = normalFoods;
    }

    public SuperFood getSuperFood() {
        return superFood;
    }

    public void setSuperFood(SuperFood superFood) {
        this.superFood = superFood;
    }
}
