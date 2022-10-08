package Foods;

import Block.Node;

import java.io.Serializable;
import java.util.Random;

public class Food extends Node {
    private static final long serialVersionUID = 1142152905673810680L;
    //食物的位置以及食物是否已经被
    public boolean isLive;
    Random random = new Random();
    public Food() {
        init();
    }

    public void init() {
        isLive = true;
        // 初始化随机的 x 和 y 坐标
        x = random.nextInt(40) + 1;
        y = random.nextInt(25) + 5;
    }
}
