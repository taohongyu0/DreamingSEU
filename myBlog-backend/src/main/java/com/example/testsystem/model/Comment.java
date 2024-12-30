package com.example.testsystem.model;

import java.time.LocalDateTime;
import java.util.List;

public class Comment {
    int id;
    int authorId;
    String authorName; //不在数据库里
    String authorUsername;
    int articleId;
    String content;
    LocalDateTime launchTime;
    int likes;
    int dislikes;
    int likeExist; //该用户是否点过赞
    int dislikeExist; //该用户是否点过踩

    public Comment(){}

    public Comment(int authorId,int articleId,String content){
        this.authorId = authorId;
        this.articleId = articleId;
        this.content = content;
        this.launchTime = LocalDateTime.now();
        this.likes = 0;
        this.dislikes = 0;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }

    public int getLikeExist() {
        return likeExist;
    }

    public void setLikeExist(int likeExist) {
        this.likeExist = likeExist;
    }

    public int getDislikeExist() {
        return dislikeExist;
    }

    public void setDislikeExist(int dislikeExist) {
        this.dislikeExist = dislikeExist;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getLaunchTime() {
        return launchTime;
    }

    public void setLaunchTime(LocalDateTime launchTime) {
        this.launchTime = launchTime;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }
}
