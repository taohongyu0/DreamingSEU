package com.example.testsystem.model;

import java.time.LocalDateTime;

public class Token {
    int id;
    int userId;
    String tokenStr;
    LocalDateTime produceTime;

    public Token(){}

    public Token(int userId,String tokenStr){
        this.userId = userId;
        this.tokenStr = tokenStr;
        this.produceTime = LocalDateTime.now();
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

    public String getTokenStr() {
        return tokenStr;
    }

    public void setTokenStr(String tokenStr) {
        this.tokenStr = tokenStr;
    }

    public LocalDateTime getProduceTime() {
        return produceTime;
    }

    public void setProduceTime(LocalDateTime produceTime) {
        this.produceTime = produceTime;
    }
}
