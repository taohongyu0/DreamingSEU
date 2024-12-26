package com.example.testsystem.service;

import com.example.testsystem.Util.ResponseMessage;
import com.example.testsystem.model.User;

import java.util.List;

public interface AdminService {
    List<User> getAllUserBriefInfo();
    ResponseMessage<String> banUser(int userId);

    ResponseMessage<String> clearPic();
}
