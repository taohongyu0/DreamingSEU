package com.example.testsystem.model;

import com.example.testsystem.mapper.CollectionMapper;

public class Collection {
    int id;
    int userId;
    int articleId;

    public Collection(){}

    public Collection(int userId,int articleId){
        this.userId = userId;
        this.articleId = articleId;
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

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }
}
