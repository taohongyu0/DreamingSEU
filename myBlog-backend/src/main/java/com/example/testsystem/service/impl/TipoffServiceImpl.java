package com.example.testsystem.service.impl;

import com.example.testsystem.Util.ResponseMessage;
import com.example.testsystem.mapper.TipoffMapper;
import com.example.testsystem.mapper.TokenMapper;
import com.example.testsystem.model.Tipoff;
import com.example.testsystem.model.Token;
import com.example.testsystem.service.TipoffService;
import com.example.testsystem.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TipoffServiceImpl implements TipoffService {
    @Autowired
    TipoffMapper tipoffMapper;
    @Autowired
    TokenMapper tokenMapper;
    @Override
    public ResponseMessage<String> addTipoff(Tipoff tipoff) {
        Token userToken = tokenMapper.getTokenByTokenStr(tipoff.getUserToken());
        if(userToken==null){
            return ResponseMessage.fail(500,"token无效");
        }
        tipoff.setUserId(userToken.getUserId());
        Tipoff toMapperTipoff = new Tipoff(tipoff.getUserId(), tipoff.getTextType(), tipoff.getTextId(), tipoff.getContent());
        tipoffMapper.addTipoff(toMapperTipoff);
        return ResponseMessage.success("success");
    }

    @Override
    public ResponseMessage<String> solve(int id) {
        tipoffMapper.solveTipoff(id);
        return ResponseMessage.success("success");
    }
}
