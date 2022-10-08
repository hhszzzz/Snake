package Block;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author hhs
 * @version 1.0
 * 方块的顶级父类
 * 对蛇、食物、子弹、障碍物的终极管理！！！
 */
public class Node implements Serializable {

    private static final long serialVersionUID = 8547314215130878891L;
    public int x;
    public int y;

    public Node() {

    }

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return y;
    }

    // 重写 equals 和 hashcode 方法
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Node)) return false;
        Node node = (Node) obj;
        return x == node.x && y == node.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Node{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
