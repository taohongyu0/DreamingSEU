package com.example.testsystem.service;

import com.example.testsystem.Util.ResponseMessage;
import com.example.testsystem.model.User;

import java.util.List;

public interface AdminService {
    List<User> getAllUserBriefInfo();
    ResponseMessage<String> banUser(int userId);
    ResponseMessage<String> banU1(int userId); //只封号，不解封
    ResponseMessage<String> banArticleComment(int articleId); //封禁某篇文章的评论区
    ResponseMessage<String> banArticleComment1(int articleId); //只封禁，不解封
    ResponseMessage<String> clearPic();
}
