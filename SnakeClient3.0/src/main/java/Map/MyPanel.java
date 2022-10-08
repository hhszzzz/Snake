package Map;

import Block.Node;
import Client.service.ManageClientConnectServerThread;
import comment.Content;
import comment.ManageContent;
import Foods.*;
import Snakes.*;
import Shots.*;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Vector;


@SuppressWarnings("all")
public class MyPanel extends JPanel implements Runnable {
    //导入图片
    Image interfaceImg = null, backgoundImg = null, white = null;

    public String userId = ManageClientConnectServerThread.getUserId();
    public P1 p1 = null;
    public Vector<P1> snakes = new Vector<>();
    public SuperFood superFood = ManageContent.getContent(userId).getSuperFood();
    public HashSet<Node> overlap = ManageContent.getContent(userId).getObstacles();
    public HashSet<NormalFood> normalFoods = ManageContent.getContent(userId).getNormalFoods();

    public MyPanel() { // 所有元素的初始化
        // p1是客户端操控的
        // snakes 画出来映射其他客户端
        for (P1 snake : ManageContent.getContent(userId).getSnake()) {
            if (snake.getUserId().equals(userId)) p1 = snake;
            snakes.add(snake);
        }
//        interfaceImg = Toolkit.getDefaultToolkit().getImage(MyPanel.class.getResource("photo\\bg1.png"));
//        backgoundImg = Toolkit.getDefaultToolkit().getImage(MyPanel.class.getResource("photo\\bg2.png"));
//        white = Toolkit.getDefaultToolkit().getImage(MyPanel.class.getResource("photo\\white.png"));
        new Thread(superFood).start();
    }

    @Override
    public void paint(Graphics g) {
        super.paintComponent(g);
        drawPassage(g);
    }

    //绘制全部界面
    public void drawPassage(Graphics g) {
        super.paintComponent(g);

        g.fillRect(0, 0, 960, 632);
        g.drawImage(white, 0, 0, 974, 82, this);
        g.setColor(Color.GRAY);
        g.setFont(new Font("微软雅黑", Font.BOLD, 50));
        g.drawString("Player1:   " + (p1.length - 3), 50, 60);
        g.setFont(new Font("微软雅黑", Font.BOLD, 50));
//        g.drawString("Player2:   " + (p2.length - 3), 550, 60);
        g.drawImage(backgoundImg, 0, 82, 974, 560, this);

        // 画蛇
        for (P1 snake : snakes) {
            drawSnake(snake, g);
        }
        // 如果两条蛇都死亡，停止画食物
//        if (p1.isLive || p2.isLive) {
        drawObstacle(overlap, g);
        drawFood(g);
        for (P1 snake : snakes) {
            if (snake.isLive) {
                drawBullet(snake.bullet, g);
            }
        }
    }

    //画障碍物
    public void drawObstacle(HashSet<Node> obstacles, Graphics g) {
        g.setColor(Color.ORANGE);
        for (Node node : overlap) {
            g.fill3DRect(node.x * 20, node.y * 20 + 2, 20, 20, false);
        }
    }

    //画蛇
    public void drawSnake(Snake snake, Graphics g) {
        // 蛇存活才绘画
        if (snake.isLive) {
            // 画出蛇头（注意区分玩家）
            if (snake.getUserId().equals(p1.getUserId())) g.setColor(Color.YELLOW);
            else g.setColor(Color.cyan);
            g.fill3DRect(snake.body.get(0).x, snake.body.get(0).y, 20, 20, false); // 设定20像素的正方形
            // 画出蛇身
            for (int i = snake.length - 1; i > 0; i--) {
                g.setColor(Color.PINK);
                g.fill3DRect(snake.body.get(i).x, snake.body.get(i).y, 20, 20, false);
            }
        } // else 死亡动画，（修改下）

        // 加速特效
        if (snake.accelerate) {
            g.setColor(Color.RED);
            Node head = snake.body.get(0);
            for (int i = 0; i < 3; i++) { // 画三个 . 代表加速
                switch (snake.getDirection()) {
                    case 0, 2 -> { // 上
                        g.fill3DRect(head.x - 5, head.y + i * 6, 3, 3, false);
                        g.fill3DRect(head.x + 25, head.y + i * 6, 3, 3, false);
                    }
                    case 1, 3 -> { // 左
                        g.fill3DRect(head.x + i * 6, head.y - 5, 3, 3, false);
                        g.fill3DRect(head.x + i * 6, head.y + 25, 3, 3, false);
                    }
                }
            }
        }

    }

    // 绘画子弹
    public void drawBullet(Bullet bullet, Graphics g) {
        if (bullet != null && bullet.isLive) {
            g.setColor(Color.YELLOW); //子弹颜色
            g.fillOval(bullet.getX(), bullet.getY(), 20, 20); // 填充圆内
            g.drawOval(bullet.getX(), bullet.getY(), 20, 20);
        }
    }

    // 绘画食物
    public void drawFood(Graphics g) {
        for (NormalFood normalFood : normalFoods) {
            if (normalFood.isLive) {
                // 绘画食物
                g.setColor(Color.GREEN);
                g.fill3DRect(normalFood.getX() * 20, normalFood.getY() * 20 + 2, 20, 20, false);
            }
        }

        // 画特殊食物
        if (superFood.isLive) {
            g.setColor(Color.YELLOW);
            g.fillOval(superFood.getX() * 20, superFood.getY() * 20 + 2, 20, 20);
            g.drawOval(superFood.getX() * 20, superFood.getY() * 20 + 2, 20, 20);
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

            snakes = new Vector<>();
            snakes.add(p1);
            for (P1 snake : ManageContent.getContent(userId).getSnake()) {
                if (!snake.getUserId().equals(p1.getUserId())) snakes.add(snake);
            }
            superFood = ManageContent.getContent(userId).getSuperFood();
            overlap = ManageContent.getContent(userId).getObstacles();
            normalFoods = ManageContent.getContent(userId).getNormalFoods();

            // 蛇动
            for (P1 snake : snakes) {
                snake.move();
            }

            // 我方子弹是否击中敌方
//            p1.hitSnake(p2);
//            p2.hitSnake(p1);

            // 对于每一个食物，判断是否被蛇吃了
            for (NormalFood normalFood : normalFoods) {
                // 吃普通食物
                for (P1 snake : snakes) {
                    snake.eat(normalFood);
                }
            }

            // 吃特殊食物
            for (P1 snake : snakes) {
                snake.eat(superFood);
            }

            // 判断两蛇是否相撞
            for (P1 snake1 : snakes) {
                for (P1 snake2 : snakes) {
                    if (snake1 != snake2)
                        snake1.collision(snake2);
                }
            }

            // 判断蛇是否与障碍物相撞
            for (P1 snake : snakes) {
                snake.collision();
            }

            // 储存信息
            Content content = new Content();
            content.setObstacles(overlap);
            content.setSnake(snakes);
            content.setNormalFoods(normalFoods);
            content.setSuperFood(superFood);
            // 将自己的 content加入
            ManageContent.addContent(userId, content);

            // 重绘
            repaint();
        }
    }
}