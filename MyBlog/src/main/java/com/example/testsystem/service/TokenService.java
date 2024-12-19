package com.example.testsystem.service;

import com.example.testsystem.Util.ResponseMessage;
import com.example.testsystem.model.Token;

public interface TokenService {
    ResponseMessage<String> quit(String tokenStr);
    void maintain(); //维护token数据表
    ResponseMessage<Integer> getUserIdByToken(String tokenStr);
}
