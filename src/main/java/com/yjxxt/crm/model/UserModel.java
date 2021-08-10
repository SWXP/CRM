package com.yjxxt.crm.model;

import com.yjxxt.crm.bean.User;
import com.yjxxt.crm.utils.UserIDBase64;

public class UserModel {
    private String userIdStr;
    private String userName;
    private String trueName;

    public UserModel() {
    }

    public UserModel(User user) {
        this.userIdStr = UserIDBase64.encoderUserID(user.getId());
        this.userName = user.getUserName();
        this.trueName = user.getTrueName();
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "userIdStr='" + userIdStr + '\'' +
                ", userName='" + userName + '\'' +
                ", trueName='" + trueName + '\'' +
                '}';
    }

    public String getUserIdStr() {
        return userIdStr;
    }

    public void setUserIdStr(String userId) {
        this.userIdStr = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }
}
