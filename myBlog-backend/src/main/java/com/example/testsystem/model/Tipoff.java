package com.example.testsystem.model;

import java.time.LocalDateTime;

public class Tipoff {
    String userToken;
    int id;
    int userId;  //举报者
    int defendantUserId; //被举报者
    String username;
    String defendantUsername; //被举报者的用户名
    int textType;  //举报的文段类型
    int textId;  //举报的文段id
    int articleId; //对应的博文id
    String articleTitle; //对应的博文名称
    LocalDateTime time; //用户举报时间
    String content; //举报者填写的内容
    boolean isSolve; //是否解决
    String commentContent;

    public Tipoff(){}

    public Tipoff(int userId,int textType,int textId,String content){
        this.userId = userId;
        this.textType = textType;
        this.textId = textId;
        this.time = LocalDateTime.now();
        this.content = content;
        this.isSolve = false;
    }

    public int getDefendantUserId() {
        return defendantUserId;
    }

    public void setDefendantUserId(int defendantUserId) {
        this.defendantUserId = defendantUserId;
    }

    public String getDefendantUsername() {
        return defendantUsername;
    }

    public void setDefendantUsername(String defendantUsername) {
        this.defendantUsername = defendantUsername;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
