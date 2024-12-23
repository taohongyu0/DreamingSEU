package com.example.testsystem.service;

import com.example.testsystem.Util.ResponseMessage;
import com.example.testsystem.model.User;
import com.example.testsystem.model.supplement.PersonalCenterInfo;
import com.example.testsystem.model.toback.FileAndToken;
import com.example.testsystem.model.toback.RoleIdAndToken;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    ResponseMessage<String> register(User user);
    ResponseMessage<RoleIdAndToken> login(User user);
    ResponseMessage<String> delUser(int userId);
    ResponseMessage<String> forgetPassword(User user);
    ResponseMessage<String> uploadProfile(MultipartFile file,String token);
    PersonalCenterInfo getCenterInfoByTokenStr(String tokenStr);
    PersonalCenterInfo getCenterInfoByUserId(int userId);
}
