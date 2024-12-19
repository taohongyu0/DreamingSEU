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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    @PostMapping("/othersCenterInfo")
    public PersonalCenterInfo getOthersCenterInfoByTokenStr(@RequestBody String userId){
        return userService.getCenterInfoByUserId(Integer.parseInt(userId));
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/del")
    public ResponseMessage<String> delUser(@RequestBody String userId){
        return userService.delUser(Integer.parseInt(userId));
    }
}
