package com.example.testsystem.model.supplement;

import java.time.LocalDateTime;

public class PersonalCenterInfo {
    double reputation; //声望值
    int totalHits; //发表的博文被点击总量
    LocalDateTime loginTime; //最后登录时间
    String loginTimeChinese; //中文版的最后登录时间
    boolean profileExist; //头像是否存在
    String username; //用户名
    String name; //昵称
    int rank; //排名
    int id;

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getReputation() {
        return reputation;
    }

    public void setReputation(double reputation) {
        this.reputation = reputation;
    }

    public int getTotalHits() {
        return totalHits;
    }

    public void setTotalHits(int totalHits) {
        this.totalHits = totalHits;
    }

    public LocalDateTime getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(LocalDateTime loginTime) {
        this.loginTime = loginTime;
    }

    public boolean isProfileExist() {
        return profileExist;
    }

    public void setProfileExist(boolean profileExist) {
        this.profileExist = profileExist;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLoginTimeChinese() {
        return loginTimeChinese;
    }

    public void setLoginTimeChinese(String loginTimeChinese) {
        this.loginTimeChinese = loginTimeChinese;
    }
}
