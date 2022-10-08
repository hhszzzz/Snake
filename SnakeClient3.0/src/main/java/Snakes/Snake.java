package Snakes;

import Block.Node;
import Foods.Food;
import Foods.SuperFood;
import Shots.Bullet;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Vector;

public class Snake implements Serializable {
    private static final long serialVersionUID = 6458507100875409712L;
    // 每一条蛇都有对应的Id与之绑定
    private String userId;
    public Vector<Node> body = new Vector<>();
    public int length = 3; // 蛇的长度
    public boolean isLive = true; // 蛇的存活状态
    private int direction; // 蛇的方向：0-上 1-左 2-下 3-右
    public final int stride = 20; // 步幅
    protected boolean chanceToShot = false; // 是否可以射击
    public Bullet bullet = null;
    public HashSet<Node> overlap = null;
    protected Node tail = null;
    // 是否处于加速状态
    public boolean accelerate = false;

    public Snake(HashSet<Node> overlap) {
        this.overlap = overlap;
    }

    // 发出子弹
    public void shot() {
        if (isLive) {
            Node head = body.get(0);
            bullet = switch (direction) {
                case 0 -> // 向上
                        new Bullet(new Node(head.x, head.y - 20), 0, overlap);
                case 1 -> // 向左
                        new Bullet(new Node(head.x - 20, head.y), 1, overlap);
                case 2 -> // 向下
                        new Bullet(new Node(head.x, head.y + 20), 2, overlap);
                case 3 -> // 向右
                        new Bullet(new Node(head.x + 20, head.y), 3, overlap);
                default -> null;
            };
            // 开启子弹线程
            new Thread(bullet).start();
            chanceToShot = false;
        }
    }

    // 我方子弹是否击中敌人
    public void hitSnake(Snake enemy) {
        if (bullet == null || !bullet.isLive) return; // 如果子弹不存在或子弹死亡，则不执行该函数
        // 判断蛇是否被子弹击中
        for (int i = 0; i < enemy.length; i ++ ) {
            if (enemy.body.get(i).x == bullet.getX() && enemy.body.get(i).y == bullet.getY()) {
                // 蛇断一节
                {
                    if (i == 0) i = 1; // 蛇不能死，至少有一个头
                    for (int j = i; j < enemy.length; j++) enemy.body.removeElementAt(j);
                    enemy.length -= (enemy.length - i);
                }
                bullet.isLive = false;
                return;
            }
        }
    }

    // 蛇吃食物
    public void eat(Food food) {
        // 判断蛇是否吃到食物
        if (body.get(0).x == food.getX() * 20
                && body.get(0).y == food.getY() * 20 + 2) {
            food.isLive = false; // 如果吃到食物，让食物死亡

            // 蛇吃完食物的逻辑
            {
                // 如果吃的是特殊食物，则可以射击一次
                if (food instanceof SuperFood) {
                    chanceToShot = true;
                }

                // 往后增加一节
                body.add(tail);
                // 蛇的长度加1
                length++;
            }
        }

        // 特殊食物有特殊的线程，在此不做处理，在SuperFood类内部处理
        if (food instanceof SuperFood) return;

        // 如果蛇吃到了普通食物，让食物生成一个新的坐标
        if (!food.isLive) {
            // 重新返回食物的xy坐标
            food.init();
        }
    }

    // 判断敌人是否撞上我方
    public void collision(Snake enemy) {
        // 对敌方进行判断
        for (int i = 1; i < length; i ++ )
            // 如果敌方蛇头撞上了我方，就让敌方死亡
            if (enemy.body.get(0).x == body.get(i).x && enemy.body.get(0).y == body.get(i).y) {
                enemy.isLive = false;
                return;
            }
    }
    // 判断蛇是否和障碍物相撞
    public void collision() {
//        for (Obstacle ob : obs) {
//            for (Node node : ob.nodes) {
//                if (node.x * 20 == body.get(0).x && node.y * 20 + 2 == body.get(0).y) {
//                    isLive = false;
//                    return;
//                }
//            }
//        }
        // 新型判重方法
        Node head = new Node(body.get(0).x / 20, (body.get(0).y - 2) / 20);
        if (overlap.contains(head)) isLive = false;
    }
    // 整条蛇的行动
    public void move() {
        // 记录尾巴的位置
        tail = body.get(length - 1);
        // 蛇身往前进一格子
        for (int i = length - 1; i > 0; i -- ) {
            body.setElementAt(body.get(i - 1), i);
        }

        Node head = body.get(0);
        // 根据当前蛇的方向确定蛇头行走的方向
        switch (direction) {
            case 0 -> // 上
                    body.setElementAt(new Node(head.x, head.y - stride), 0);
            case 1 -> // 左
                    body.setElementAt(new Node(head.x - stride, head.y), 0);
            case 2 -> // 下
                    body.setElementAt(new Node(head.x, head.y + stride), 0);
            case 3 -> // 右
                    body.setElementAt(new Node(head.x + stride, head.y), 0);
            default -> System.out.println("暂不做处理");
        }

        // 有可能会经过上一步对头部进行改变，重新赋值
        head = body.get(0);
        // 如果蛇撞墙，就会死亡
        if (!(head.x >= 0 && head.x <= 940 && head.y >= 82 && head.y <= 600 && isLive))
            isLive = false;

        // 如果蛇撞到自己，也会死亡
        for (int i = 1; i < this.length; i ++ ) {
            if (head.x == body.get(i).x && head.y == body.get(i).y && isLive) {
                isLive = false;
                break;
            }
        }
    }

    //getter和Setter
    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getStride() {
        return stride;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
