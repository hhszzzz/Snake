package comment;

import java.io.Serializable;

/**
 * @author hhs
 * @version 1.0
 * 表示一个用户/客户信息
 */
public class User implements Serializable {

    private static final long serialVersionUID = -3237995642268201941L;
    private String userId; // 用户ID/用户名
    private String password; // 密码

    public User() {

    }
    public User(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
