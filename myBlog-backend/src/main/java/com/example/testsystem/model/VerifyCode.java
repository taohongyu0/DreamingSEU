package com.example.testsystem.model;

import java.time.LocalDateTime;

public class VerifyCode {
    int id;
    String email;
    String code;
    LocalDateTime produceTime;
    boolean valid;

    public VerifyCode(){}

    public VerifyCode(String email,String code){
        this.email = email;
        this.code = code;
        this.produceTime = LocalDateTime.now();
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getProduceTime() {
        return produceTime;
    }

    public void setProduceTime(LocalDateTime produceTime) {
        this.produceTime = produceTime;
    }
}
