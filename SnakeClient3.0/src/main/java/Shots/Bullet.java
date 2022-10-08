package Shots;

import Block.Node;

import java.util.HashSet;

/**
 * @author hhs
 * @version 1.0
 */
public class Bullet extends Node implements Runnable {
    private static final long serialVersionUID = 3313716516808004090L;
    private int direction; // 子弹方向
    private final int speed = 20; // 子弹速度
    public boolean isLive = true; // 记录子弹是否存活
    public HashSet<Node> overlap = null;

    public Bullet(Node node, int direction, HashSet<Node> overlap) {
        this.x = node.x;
        this.y = node.y;
        this.direction = direction;
        this.overlap = overlap;
    }

    public int getDirection() {
        return direction;
    }

    public void move() {
        // 根据方向改变x, y坐标，0：上 1：左 2：下 3：右
        switch (direction) {
            case 0 -> y -= speed;
            case 1 -> x -= speed;
            case 2 -> y += speed;
            case 3 -> x += speed;
            default -> System.out.println("暂不考虑该情况");
        }
    }

    // 子弹与障碍物碰撞
    public void collision() {
        Node bullet = new Node(x / 20, (y - 2) / 20);
        if (overlap.contains(bullet)) {
            isLive = false;
            overlap.remove(bullet);
        }
    }

    @Override
    public void run() {
        // 让子弹休眠实现动态
        while (true) {
            try {
                Thread.sleep(60); // 80ms移动20px（speed）
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            // 子弹移动
            move();
            // 子弹是否与障碍物相撞
            collision();

            //子弹销毁的两种情况：1.碰到敌人，2.碰到边界
            if (!(x >= 0 && x <= 960 && y >= 92 && y <= 632 && isLive)) {
                isLive = false;
                break;
            }
        }
    }
}
