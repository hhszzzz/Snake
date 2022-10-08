import Map.ServerPanel;
import Server.service.SnakeServer;

/**
 * @author hhs
 * @version 1.0
 */
public class SnakeFrame {
    public static void main(String[] args) {
        new Thread(new ServerPanel()).start();
        SnakeServer snakeServer = new SnakeServer();
    }
}
