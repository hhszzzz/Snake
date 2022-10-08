package Snakes;

import Block.Node;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;

public class P2 extends Snake implements KeyListener {
    private static final long serialVersionUID = -6566524424306628355L;
    // 是否处于加速状态
    public boolean accelerate = false;
    public P2(HashSet<Node> overlap) {
        super(overlap);
        // P2设定向左
        setDirection(1);
        for (int i = 0; i < length; i ++ ) // 初始化蛇的位置
            body.add(new Node(i * getStride() + 800, 122));
        tail = body.get(length - 1);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> { // 上
                if (this.getDirection() == 2 || this.getDirection() == 0) break; // 如果出现相反方向的就退出
                this.setDirection(0); // 方向设置
            }
            case KeyEvent.VK_LEFT -> { // 左
                if (this.getDirection() == 3 || this.getDirection() == 1) break;
                this.setDirection(1);
            }
            case KeyEvent.VK_DOWN -> { // 下
                if (this.getDirection() == 0 || this.getDirection() == 2) break;
                this.setDirection(2);
            }
            case KeyEvent.VK_RIGHT -> { // 右
                if (this.getDirection() == 1 || this.getDirection() == 3) break;
                this.setDirection(3);
            }
            case KeyEvent.VK_NUMPAD3 -> {
                move(); // 加速
                accelerate = true;
            }
            default -> {
            }
        }

        // P2按下小键盘的 1射击
        if (e.getKeyCode() == KeyEvent.VK_NUMPAD1 && chanceToShot) {
            shot();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_NUMPAD3) {
            accelerate = false;
        }
    }
}
