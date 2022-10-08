package Obstacle;

import Block.Node;

import java.io.Serializable;
import java.util.Random;
import java.util.Vector;

/**
 * @author hhs
 * @version 1.0
 */
public class Obstacle extends Node implements ObstaclesType {

    private static final long serialVersionUID = -6221197555207625252L;
    protected int direction;
    public int type;
    public final int length = 4;
    public Vector<Node> nodes = null;

    public Obstacle() {
        init();
    }

    public void init() {
        Random random = new Random();
        x = random.nextInt(33) + 3;
        y = random.nextInt(17) + 12;
        direction = random.nextInt(4); // 方向 0~3

        nodes = new Vector<>();
        // 规定障碍物的类型
        type = random.nextInt(4);
        switch (type) {
            case 0 -> initL();
            case 1 -> initZ();
            case 2 -> initO();
            case 3 -> initI();
        }
    }

    @Override
    public void initL() {
        nodes.add(new Node(x, y));
        // 每个方向对应不同的障碍物
        switch (direction) {
            case 0 -> { // 上：L（顺时针转、下面(O)是起点）
                //(O)
                //O
                //O O
                nodes.add(new Node(x, y + 1));
                nodes.add(new Node(x, y + 2));
                nodes.add(new Node(x + 1, y + 2));
            }
            case 1 -> { // 左
                //    O
                //(O) O O
                nodes.add(new Node(x + 1, y));
                nodes.add(new Node(x + 2, y));
                nodes.add(new Node(x + 2, y - 1));
            }
            case 2 -> { // 下
                //O O
                //O
                //(O)
                nodes.add(new Node(x, y - 1));
                nodes.add(new Node(x, y - 2));
                nodes.add(new Node(x - 1, y - 2));
            }
            case 3 -> { // 右
                //O O (O)
                //O
                nodes.add(new Node(x - 1, y));
                nodes.add(new Node(x - 2, y));
                nodes.add(new Node(x - 2, y + 1));
            }
        }
    }

    @Override
    public void initZ() {
        nodes.add(new Node(x, y));
        // 每个方向对应不同的障碍物
        switch (direction) {
            case 0 -> { // 上
                // (O)
                //O O
                //O
                nodes.add(new Node(x, y + 1));
                nodes.add(new Node(x - 1, y + 1));
                nodes.add(new Node(x - 1, y + 2));
            }
            case 1 -> { // 左
                //(O) O
                //    O O
                nodes.add(new Node(x + 1, y));
                nodes.add(new Node(x + 1, y + 1));
                nodes.add(new Node(x + 2, y + 1));
            }
            case 2 -> { // 下
                //   O
                // O O
                //(O)
                nodes.add(new Node(x, y - 1));
                nodes.add(new Node(x + 1, y - 1));
                nodes.add(new Node(x + 1, y - 2));
            }
            case 3 -> { // 右
                //O O
                //  O (O)
                nodes.add(new Node(x - 1, y));
                nodes.add(new Node(x - 1, y - 1));
                nodes.add(new Node(x - 2, y - 1));
            }
        }
    }

    @Override
    public void initO() {
        nodes.add(new Node(x, y));
        switch (direction) {
            case 0 -> {
                //(O) O
                // O  O
                nodes.add(new Node(x + 1, y));
                nodes.add(new Node(x + 1, y + 1));
                nodes.add(new Node(x, y + 1));
            }
            case 1 -> {
                // O  O
                //(O) O
                nodes.add(new Node(x + 1, y));
                nodes.add(new Node(x + 1, y - 1));
                nodes.add(new Node(x, y + 1));
            }
            case 2 -> {
                //O (O)
                //O  O
                nodes.add(new Node(x - 1, y));
                nodes.add(new Node(x - 1, y + 1));
                nodes.add(new Node(x, y + 1));
            }
            case 3 -> {
                //O  O
                //O (O)
                nodes.add(new Node(x, y - 1));
                nodes.add(new Node(x - 1, y - 1));
                nodes.add(new Node(x - 1, y));
            }
        }
    }

    @Override
    public void initI() {
        nodes.add(new Node(x, y));
        switch (direction) {
            case 0 -> {
                //(O)
                // O
                // O
                // O
                nodes.add(new Node(x, y - 1));
                nodes.add(new Node(x, y - 2));
                nodes.add(new Node(x, y - 3));
            }
            case 1 -> {
                //(O) O O O
                nodes.add(new Node(x + 1, y));
                nodes.add(new Node(x + 2, y));
                nodes.add(new Node(x + 3, y));
            }
            case 2 -> {
                // O
                // O
                // O
                //(O)
                nodes.add(new Node(x, y + 1));
                nodes.add(new Node(x, y + 2));
                nodes.add(new Node(x, y + 3));
            }
            case 3 -> {
                //O O O (O)
                nodes.add(new Node(x - 1, y));
                nodes.add(new Node(x - 2, y));
                nodes.add(new Node(x - 3, y));
            }
        }
    }
}
