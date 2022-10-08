package Map;

import Client.service.view.SnakeView;
import Snakes.Snake;

import javax.swing.*;

public class Map extends JFrame implements Runnable {
    public static void main(String[] args) {
        Init();
    }

    public static void Init(){

        MyPanel myPanel=new MyPanel();
        // 设定标题
        JFrame frame= new JFrame("Snake");
        // 规定组件高宽和起始位置
        frame.setBounds(300,100,974,642);
        // 窗口大小不可更改
        frame.setResizable(false);
        // 设定关闭窗口就退出程序
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 画板加入到画框
        frame.add(myPanel);
        // 开启画板的线程
        new Thread(myPanel).start();
        frame.addKeyListener(myPanel.p1);
        // 设置窗口是否可见
        frame.setVisible(true);
    }

    @Override
    public void run() {
        Init();
    }
}
