package com.example.testsystem.model;

import com.example.testsystem.model.inRedis.ArticleInRedis;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Article {
    int id;
    String title;
    int authorId;
    String authorName;  //这个参数有时会显示，但不往数据库里传
    String content;
    LocalDateTime createTime;
    LocalDateTime modifyTime;
    int hits; //点击次数
    int likes; //喜欢次数
    int dislikes; //点踩次数
    int collect; //收藏次数
    boolean allowComment; //是否允许评论
    List<Comment> comments; //评论的列表
    int boardId;
    int likeExist; //是否点过赞
    int dislikeExist; //是否点过踩
    int collectExist; //是否收藏过
    String cover; //封面文件名

    public Article(){}

    public Article(String title,String content,int authorId,int boardId,String cover){
        this.title = title;
        this.authorId = authorId;
        this.content = content;
        this.boardId = boardId;
        LocalDateTime now = LocalDateTime.now();
        this.createTime = now;
        this.modifyTime = now;
        this.hits = 0;
        this.likes = 0;
        this.dislikes = 0;
        this.collect = 0;
        this.allowComment = true;
        this.cover = cover;
    }

    public Article(ArticleInRedis a){
        this.id = a.getId();
        this.title = a.getTitle();
        this.authorId = a.getAuthorId();
        this.authorName = a.getAuthorName();
        this.content = a.getContent();
        // 创建一个DateTimeFormatter实例，指定输出格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

//        // 将LocalDateTime转换为String
//        String formattedDateTime = now.format(formatter);

        // 将字符串转换为LocalDateTime
        this.createTime = LocalDateTime.parse(a.getCreateTime(), formatter);
        this.modifyTime = LocalDateTime.parse(a.getModifyTime(),formatter);
        this.hits = a.getHits();
        this.likes = a.getLikes();
        this.dislikes = a.getDislikes();
        this.collect = a.getCollect();
        this.allowComment = a.isAllowComment();
        this.comments = a.getComments();
        this.boardId = a.getBoardId();
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
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

    public int getCollectExist() {
        return collectExist;
    }

    public void setCollectExist(int collectExist) {
        this.collectExist = collectExist;
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

    public int getBoardId() {
        return boardId;
    }

    public void setBoardId(int boardId) {
        this.boardId = boardId;
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

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDateTime modifyTime) {
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
}
