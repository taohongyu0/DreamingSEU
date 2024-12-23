package com.example.testsystem.model.toback;

public class LikeArticleToBack {
    String tokenStr;
    int articleId;
    int textType;

    public int getTextType() {
        return textType;
    }

    public void setTextType(int textType) {
        this.textType = textType;
    }

    public String getTokenStr() {
        return tokenStr;
    }

    public void setTokenStr(String tokenStr) {
        this.tokenStr = tokenStr;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }
}
