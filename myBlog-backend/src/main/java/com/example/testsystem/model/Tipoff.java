package com.example.testsystem.model;

import java.time.LocalDateTime;

public class Tipoff {
    String userToken;
    int id;
    int userId;  //举报者
    int textType;  //举报的文字类型
    int textId;  //举报的文字id
    LocalDateTime time; //用户举报时间
    String content;
    boolean isSolve; //是否解决

    public Tipoff(){}

    public Tipoff(int userId,int textType,int textId,String content){
        this.userId = userId;
        this.textType = textType;
        this.textId = textId;
        this.time = LocalDateTime.now();
        this.content = content;
        this.isSolve = false;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTextType() {
        return textType;
    }

    public void setTextType(int textType) {
        this.textType = textType;
    }

    public int getTextId() {
        return textId;
    }

    public void setTextId(int textId) {
        this.textId = textId;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isSolve() {
        return isSolve;
    }

    public void setSolve(boolean solve) {
        this.isSolve = solve;
    }
}
