package com.example.testsystem.model.inRedis;

import com.example.testsystem.model.Article;
import com.example.testsystem.model.Comment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ArticleInRedis {
    int id;
    String title;
    int authorId;
    String authorName;  //这个参数有时会显示，但不往数据库里传
    String content;
    String createTime;
    String modifyTime;
    int hits; //点击次数
    int likes; //喜欢次数
    int dislikes; //点踩次数
    int collect; //收藏次数
    boolean allowComment; //是否允许评论
    List<Comment> comments; //评论的列表
    int boardId;

    public ArticleInRedis(){}

    public ArticleInRedis(Article article){
        this.id = article.getId();
        this.title = article.getTitle();
        this.authorId = article.getAuthorId();
        this.authorName = article.getAuthorName();
        this.content = article.getContent();
        // 创建一个DateTimeFormatter实例，指定输出格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // 将LocalDateTime转换为String
        this.createTime = article.getCreateTime().format(formatter);
        this.modifyTime = article.getModifyTime().format(formatter);
        this.hits = article.getHits(); //点击次数
        this.likes = article.getLikes(); //喜欢次数
        this.dislikes = article.getDislikes();
        this.collect = article.getCollect();
        this.allowComment = article.isAllowComment(); //是否允许评论
        this.comments = article.getComments(); //评论的列表
        this.boardId = article.getBoardId();
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public int getCollect() {
        return collect;
    }

    public void setCollect(int collect) {
        this.collect = collect;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public boolean isAllowComment() {
        return allowComment;
    }

    public void setAllowComment(boolean allowComment) {
        this.allowComment = allowComment;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public int getBoardId() {
        return boardId;
    }

    public void setBoardId(int boardId) {
        this.boardId = boardId;
    }
}
