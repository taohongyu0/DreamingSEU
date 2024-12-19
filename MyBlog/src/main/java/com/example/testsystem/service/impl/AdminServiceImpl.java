package com.example.testsystem.service.impl;

import com.example.testsystem.Util.ResponseMessage;
import com.example.testsystem.mapper.AdminMapper;
import com.example.testsystem.model.User;
import com.example.testsystem.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    AdminMapper adminMapper;
    @Override
    public List<User> getAllUserBriefInfo() {
        return adminMapper.getAllUserBriefInfo();
    }

    @Override
    public ResponseMessage<String> banUser(int userId) {
        User tempUser = adminMapper.getUserBriefInfoById(userId);
        if(tempUser==null){
            return ResponseMessage.fail(500,"用户不存在");
        }
        if(tempUser.isBanned()){ //已经被封了，此时要解封
            adminMapper.unbanUser(userId);
            return ResponseMessage.success("成功解封");
        }
        else{ //没被封号，要封禁
            adminMapper.banUser(userId);
            return ResponseMessage.success("成功封禁");
        }
    }
}
