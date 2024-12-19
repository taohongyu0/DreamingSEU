package com.example.testsystem.model;

import java.time.LocalDateTime;

public class Likes {
    int id;
    int userId;
    int textType;
    int textId;
    LocalDateTime time;

    public Likes(){}

    public Likes(int userId, int textType, int textId){
        this.userId = userId;
        this.textType = textType;
        this.textId = textId;
        this.time = LocalDateTime.now();
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
}
