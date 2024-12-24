package com.example.testsystem.service.impl;

import com.example.testsystem.Util.ResponseMessage;
import com.example.testsystem.mapper.*;
import com.example.testsystem.model.Likes;
import com.example.testsystem.model.Token;
import com.example.testsystem.model.inRedis.ArticleInRedis;
import com.example.testsystem.model.toback.LikeArticleToBack;
import com.example.testsystem.redis.ArticleRedis;
import com.example.testsystem.service.DislikesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DislikesServiceImpl implements DislikesService {
    @Autowired
    DislikesMapper dislikesMapper;
    @Autowired
    LikesMapper likesMapper;
    @Autowired
    TokenMapper tokenMapper;
    @Autowired
    ArticleMapper articleMapper;
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    ArticleRedis articleRedis;

    @Override
    public ResponseMessage<String> dislikeArticle(LikeArticleToBack likeArticleToBack) {
        String removeLikeStr = "";
        //先通过token获取用户id
        Token tempToken = tokenMapper.getTokenByTokenStr(likeArticleToBack.getTokenStr());
        if(tempToken==null){
            return ResponseMessage.fail(500,"token无效");
        }
        int userId = tempToken.getUserId();
        //先判断之前有没有点过赞
        int likeExist = likesMapper.recordExist(userId, likeArticleToBack.getTextType(), likeArticleToBack.getArticleId());
        if(likeExist>0){
            //点过赞，点踩时要先取赞
            likesMapper.deleteLikeRecord(userId,likeArticleToBack.getTextType(),likeArticleToBack.getArticleId());
            if(likeArticleToBack.getTextType()==1){
                //article
                articleMapper.reduceLike(likeArticleToBack.getArticleId());
            }
            else if(likeArticleToBack.getTextType()==2){
                //comment
                commentMapper.reduceLike(likeArticleToBack.getArticleId());
            }
            removeLikeStr = "取赞";
        }
        //判断有没有点过踩
        int dislikeExist = dislikesMapper.recordExist(userId, likeArticleToBack.getTextType(), likeArticleToBack.getArticleId());
        if(dislikeExist>0){
            //点过踩，取消踩
            dislikesMapper.deleteDislikeRecord(userId,likeArticleToBack.getTextType(),likeArticleToBack.getArticleId());
            if(likeArticleToBack.getTextType()==1){
                //article
                articleMapper.reduceDislike(likeArticleToBack.getArticleId());
                articleRedis.saveDataToCache(likeArticleToBack.getArticleId(),new ArticleInRedis(articleMapper.getArticleById(likeArticleToBack.getArticleId())));
            }
            else if(likeArticleToBack.getTextType()==2){
                //comment
                commentMapper.reduceDislike(likeArticleToBack.getArticleId());
            }

            return ResponseMessage.success("成功取踩"+removeLikeStr);
        }
        else{
            //没点过赞，点赞
            Likes tempLikes = new Likes(userId,likeArticleToBack.getTextType(),likeArticleToBack.getArticleId());
            dislikesMapper.addDislikeRecord(tempLikes);
            if(likeArticleToBack.getTextType()==1){
                //article
                articleMapper.addDislike(likeArticleToBack.getArticleId());
                articleRedis.saveDataToCache(likeArticleToBack.getArticleId(),new ArticleInRedis(articleMapper.getArticleById(likeArticleToBack.getArticleId())));
            }
            else if(likeArticleToBack.getTextType()==2){
                //comment
                commentMapper.addDislike(likeArticleToBack.getArticleId());
            }
            return ResponseMessage.success("成功点踩"+removeLikeStr);
        }

    }
}
