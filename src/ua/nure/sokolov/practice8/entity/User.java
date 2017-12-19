package ua.nure.sokolov.practice8.entity;

import ua.nure.sokolov.practice8.DBManager;

import java.sql.*;

public class User {
    private int userId;
    private String login;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", login='" + login + '\'' +
                '}';
    }

    public static User createUser(String s) {
        User user = new User();
        user.setLogin(s);
        return user;
    }

    public static User createUser(String s, int id) {
        User user = new User();
        user.setUserId(id);
        user.setLogin(s);
        return user;
    }
}
