package com.example.testsystem.service.impl;

import com.example.testsystem.Util.ResponseMessage;
import com.example.testsystem.mapper.TokenMapper;
import com.example.testsystem.model.Token;
import com.example.testsystem.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {
    @Autowired
    TokenMapper tokenMapper;
    @Override
    public ResponseMessage<String> quit(String tokenStr) {
        if(tokenStr==null){
            return ResponseMessage.fail(500,"no token");
        }
        tokenMapper.deleteToken(tokenStr);
        return ResponseMessage.success("成功");
    }

    @Override
    public void maintain() {
        //删除24小时以上的
    }

    @Override
    public ResponseMessage<Integer> getUserIdByToken(String tokenStr) {
        Token tempToken =  tokenMapper.getTokenByTokenStr(tokenStr);
        if(tempToken == null){
            return ResponseMessage.fail(500,"token无效");
        }
        int userId = tempToken.getUserId();
        return ResponseMessage.success(userId);
    }
}
