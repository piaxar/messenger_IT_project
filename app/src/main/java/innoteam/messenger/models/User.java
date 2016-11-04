package innoteam.messenger.models;

import java.util.ArrayList;

/**
 * Created by olejeglejeg on 04.11.16.
 */

public class User {
    private int userId;
    private String userLogin;
    private String fullnName;

    public User(int userId, String userLogin, String fullnName) {
        this.userId = userId;
        this.userLogin = userLogin;
        this.fullnName = fullnName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getFullnName() {
        return fullnName;
    }

    public void setFullnName(String fullnName) {
        this.fullnName = fullnName;
    }

    public ArrayList<User> getAllUsers(){
        return null;
    }
}
