package Foods;

/**
 * @author hhs
 * @version 1.0
 * 继承Food类
 * 实现特殊食物子弹类
 */
public class SuperFood extends Food implements Runnable {

    private static final long serialVersionUID = -5400482938812171405L;

    public SuperFood() {
        super();
    }

    @Override
    public void run() {
        while (true) {
            // 每隔十秒就生成一次特殊食物
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            init();
        }
    }
}
