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
}
