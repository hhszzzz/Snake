package Client.service.view;

import Client.service.MessageClientService;
import Client.service.UserClientService;
import Client.service.utils.Utility;
import Map.Map;

/**
 * @author hhs
 * @version 1.0
 * 客户端的菜单界面
 */
public class SnakeView {

    private boolean loop = true; // 控制是否显示菜单
    private String key = ""; // 接收用户的键盘输入
    public UserClientService userClientService = new UserClientService(); // 用于登录服务器/注册用户
    public MessageClientService messageClientService = new MessageClientService(); // 处理和消息相关

    //显示主菜单
    public void mainMenu() {

        while (loop) {

            System.out.println("===========  QQ  ===========");
            System.out.println("\t\t 1 登录系统");
            System.out.println("\t\t 9 退出系统");
            System.out.print("请输入你的选择：");
            key = Utility.readString(1); // 输入一个字符串

            // 根据用户的输入，来处理不同的逻辑
            switch (key) {
                case "1" -> {
                    System.out.print("请输入用户名：");
                    String userId = Utility.readString(50);
                    System.out.print("请输入密  码：");
                    String pwd = Utility.readString(50);

                    // 需要到服务端去验证该用户是否合法
                    // 我们这里编写一个类 UserClientService[用户登录/注册]
                    if (userClientService.checkUser(userId, pwd)) {
                        System.out.println("\n==========登陆成功==========");
                        System.out.println("==========欢迎用户（" + userId + "）==========");
                        while (loop) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            System.out.println("\n========二级菜单（用户" + userId + "）========");
                            System.out.println("\t\t 1 私      聊");
                            System.out.println("\t\t 2 群 发 消 息");
                            System.out.println("\t\t 9 退 出 系 统");
                            System.out.print("请输入你的选择：");

                            key = Utility.readString(1);
                            switch (key) {
                                case "1" -> { // 私聊
                                    String receiver = "", content = "";
                                    System.out.print("请输入要solo的用户：");
                                    receiver = Utility.readString(50);
                                    messageClientService.sendMessageToOne(receiver, userId);
                                }
                                case "2" -> { // 群发消息
                                    System.out.print("全体作战即将开始...");
                                    messageClientService.sendMessageToAll(userId);
                                }
                                case "9" -> {
                                    // 调用方法，给服务器发送一个退出系统的 message
                                    userClientService.logout();
                                    loop = false;
                                }
                                default -> {
                                }
                            }
                        }

                    } else { // 登录服务器失败
                        System.out.println("\n==========登录失败！==========");
                        System.out.println("==========请重新输入==========\n");
                    }
                }
                case "9" -> {
                    System.out.println("客户端已退出系统");
                    loop = false;
                }
            }
        }
    }
}
