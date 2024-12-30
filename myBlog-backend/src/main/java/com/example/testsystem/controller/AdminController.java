package com.example.testsystem.controller;

import com.example.testsystem.Util.ResponseMessage;
import com.example.testsystem.model.User;
import com.example.testsystem.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    AdminService adminService;

    @CrossOrigin(origins = "*")
    @PostMapping("/allUserBriefInfo")
    public List<User> getAllUserBriefInfo(){
        return adminService.getAllUserBriefInfo();
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/banUser")
    public ResponseMessage<String> banUser(@RequestBody String userId){
        return adminService.banUser(Integer.parseInt(userId));
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/banU1")
    public ResponseMessage<String> banU1(@RequestBody String userId){
        return adminService.banU1(Integer.parseInt(userId));
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/banArticleComment")
    public ResponseMessage<String> banArticleComment(@RequestBody String articleId){
        return adminService.banArticleComment(Integer.parseInt(articleId));
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/banArticleComment1")  //只禁，不解禁
    public ResponseMessage<String> banArticleComment1(@RequestBody String articleId){
        return adminService.banArticleComment1(Integer.parseInt(articleId));
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/clearPic")
    public ResponseMessage<String> clearPic(){  //清理所有没用的图片
        return adminService.clearPic();
    }
}
