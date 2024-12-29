package com.example.testsystem.service;

import com.example.testsystem.Util.ResponseMessage;
import com.example.testsystem.model.User;
import com.example.testsystem.model.supplement.PersonalCenterInfo;
import com.example.testsystem.model.toback.RoleIdAndToken;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.List;

public interface UserService {
    ResponseMessage<String> register(User user);
    ResponseMessage<RoleIdAndToken> login(User user);
    ResponseMessage<String> delUser(int userId);
    ResponseMessage<String> forgetPassword(User user);
    ResponseMessage<String> uploadProfile(MultipartFile file,String token);
    PersonalCenterInfo getCenterInfoByTokenStr(String tokenStr);
    PersonalCenterInfo getCenterInfoByUserId(int userId);
    ResponseMessage<String> changePassword(User user);
    ResponseMessage<User> getUserByUsername(String username);
    ResponseMessage<String> changeInfo(User user);
    ResponseMessage<String> setReputationRank(); //设置声望
    List<PersonalCenterInfo> getReputationRank(); //获取声望
    ResponseMessage<String> sendEmail(String emailAddress) throws MessagingException, GeneralSecurityException, UnsupportedEncodingException;
}
