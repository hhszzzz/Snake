package Snakes;

import Block.Node;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;

/**
 * 蛇1的实现
 */
public class P1 extends Snake implements KeyListener {
    private static final long serialVersionUID = 6240443289997872913L;


    // 是否处于加速状态
    public boolean accelerate = false;
    public P1(HashSet<Node> overlap, String userId) {
        super(overlap);
        // P1设定向右
        setDirection(3);
        setUserId(userId);
        for (int i = length - 1; i >= 0; i -- ) // 初始化蛇的位置
            // 根据id来分配初始位置
            body.add(new Node(i * getStride(), 22 + Integer.parseInt(userId)));
        tail = body.get(length - 1);
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    /**
     * 获取键盘按键对蛇的上下左右移动进行控制
     * @param e 键盘监听
     */
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> { // 上
                if (this.getDirection() == 2 || this.getDirection() == 0) break; // 如果出现相反方向的就退出
                this.setDirection(0); // 方向设置
            }
            case KeyEvent.VK_A -> { // 左
                if (this.getDirection() == 3 || this.getDirection() == 1) break;
                this.setDirection(1);
            }
            case KeyEvent.VK_S -> { // 下
                if (this.getDirection() == 0 || this.getDirection() == 2) break;
                this.setDirection(2);
            }
            case KeyEvent.VK_D -> { // 右
                if (this.getDirection() == 1 || this.getDirection() == 3) break;
                this.setDirection(3);
            }
            case KeyEvent.VK_L -> {
                move(); // 加速
                accelerate = true;
            }
            default -> {
            }
        }

        // P1按下J射击
        if (e.getKeyCode() == KeyEvent.VK_J && chanceToShot) {
            shot();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_L) {
            accelerate = false;
        }
    }
}
