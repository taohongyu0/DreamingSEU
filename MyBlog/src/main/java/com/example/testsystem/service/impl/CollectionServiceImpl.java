package com.example.testsystem.service.impl;

import com.example.testsystem.Util.ResponseMessage;
import com.example.testsystem.mapper.CollectionMapper;
import com.example.testsystem.mapper.TokenMapper;
import com.example.testsystem.model.Collection;
import com.example.testsystem.model.Token;
import com.example.testsystem.model.toback.CollectionToBack;
import com.example.testsystem.service.CollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CollectionServiceImpl implements CollectionService {
    @Autowired
    CollectionMapper collectionMapper;
    @Autowired
    TokenMapper tokenMapper;
    @Override
    public ResponseMessage<String> addCollection(CollectionToBack collectionToBack) {
        Token userToken = tokenMapper.getTokenByTokenStr(collectionToBack.getTokenStr());
        if(userToken==null){
            return ResponseMessage.fail(500,"token无效");
        }

        Collection tempCollect = new Collection(userToken.getUserId(), collectionToBack.getArticleId());

        //检查一下是否收藏，有则取消收藏
        int collectExist = collectionMapper.recordExist(tempCollect);
        if(collectExist>0){
            collectionMapper.removeCollection(tempCollect);
            return ResponseMessage.success("成功取收");
        }
        else{
            collectionMapper.addCollection(tempCollect);
            return ResponseMessage.success("成功收藏");
        }
    }

    @Override
    public ResponseMessage<String> removeCollection(CollectionToBack collectionToBack) {
        Token userToken = tokenMapper.getTokenByTokenStr(collectionToBack.getTokenStr());
        if(userToken==null){
            return ResponseMessage.fail(500,"token无效");
        }
        Collection tempCollect = new Collection(userToken.getUserId(), collectionToBack.getArticleId());
        collectionMapper.removeCollection(tempCollect);
        return ResponseMessage.success("成功");
    }

    @Override
    public int articleCollectNum(int articleId) {
        return collectionMapper.getCollectNumByArticleId(articleId);
    }
}
