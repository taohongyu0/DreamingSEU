package com.example.testsystem.controller;

import com.example.testsystem.Util.ResponseMessage;
import com.example.testsystem.mapper.TokenMapper;
import com.example.testsystem.model.Token;
import com.example.testsystem.model.User;
import com.example.testsystem.model.supplement.PersonalCenterInfo;
import com.example.testsystem.model.toback.FileAndToken;
import com.example.testsystem.service.TokenService;
import com.example.testsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.UUID;

@RestController  //转json文本
@RequestMapping("/user")  //loaclhost:8088/user/
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    TokenService tokenService;
    @Autowired
    TokenMapper tokenMapper;

    @CrossOrigin(origins = "*")
    @PostMapping("/register")
    public ResponseMessage register(@Validated @RequestBody User user){
        return userService.register(user);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/login")
    public ResponseMessage login(@RequestBody User user){
        return userService.login(user);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/quit")
    public ResponseMessage<String> quit(@RequestBody String tokenStr){
        return tokenService.quit(tokenStr);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/forgetPassword")
    public ResponseMessage forgetPassword(@RequestBody User user){
        return userService.forgetPassword(user);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/sendEmail")  //发送邮件，获得验证码
    public ResponseMessage<String> sendEmail(@RequestBody String emailAddress) throws MessagingException, GeneralSecurityException, UnsupportedEncodingException {
        return userService.sendEmail(emailAddress);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/uploadProfile")
    public ResponseMessage<String> uploadProfile(@RequestParam("file") MultipartFile file,@RequestParam("token") String token) {
        return userService.uploadProfile(file,token);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/personalCenterInfo")
    public PersonalCenterInfo getCenterInfoByTokenStr(@RequestBody String tokenStr){
        return userService.getCenterInfoByTokenStr(tokenStr);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/changePassword")
    public ResponseMessage changePassword(@RequestBody User user){
        return userService.changePassword(user);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/getNameAndEmail")
    public ResponseMessage<User> getUserByUsername(@RequestBody String username){
        return userService.getUserByUsername(username);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/changeInfo")
    public ResponseMessage changeInfo(@RequestBody User user){
        return userService.changeInfo(user);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/othersCenterInfo")
    public PersonalCenterInfo getOthersCenterInfoByTokenStr(@RequestBody String userId){
        return userService.getCenterInfoByUserId(Integer.parseInt(userId));
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/del")
    public ResponseMessage<String> delUser(@RequestBody String userId){
        return userService.delUser(Integer.parseInt(userId));
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/getReputationRank")
    public List<PersonalCenterInfo> getReputationRank(){
        return userService.getReputationRank();
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/setReputationRank")
    public ResponseMessage<String> setReputationRank(){
        return userService.setReputationRank();
    }
}
