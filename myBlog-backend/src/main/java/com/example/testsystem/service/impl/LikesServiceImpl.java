package com.example.testsystem.service.impl;

import com.example.testsystem.Util.ResponseMessage;
import com.example.testsystem.mapper.*;
import com.example.testsystem.model.Likes;
import com.example.testsystem.model.Token;
import com.example.testsystem.model.inRedis.ArticleInRedis;
import com.example.testsystem.model.toback.LikeArticleToBack;
import com.example.testsystem.redis.ArticleRedis;
import com.example.testsystem.service.LikesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikesServiceImpl implements LikesService {
    @Autowired
    LikesMapper likesMapper;
    @Autowired
    DislikesMapper dislikesMapper;
    @Autowired
    TokenMapper tokenMapper;
    @Autowired
    ArticleMapper articleMapper;
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    ArticleRedis articleRedis;

    @Override
    public ResponseMessage<String> likeArticle(LikeArticleToBack likeArticleToBack) {
        String removeDislikeStr = ""; //根据是否取消点踩内容添加
        //先通过token获取用户id
        Token tempToken = tokenMapper.getTokenByTokenStr(likeArticleToBack.getTokenStr());
        if(tempToken==null){
            return ResponseMessage.fail(500,"token无效");
        }
        int userId = tempToken.getUserId();
        //先判断有没有点过赞
        int likeExist = likesMapper.recordExist(userId, likeArticleToBack.getTextType(), likeArticleToBack.getArticleId());
        int dislikeExist = dislikesMapper.recordExist(userId, likeArticleToBack.getTextType(), likeArticleToBack.getArticleId());
        //先判断是否取消点踩
        if(dislikeExist>0){
            //点过踩，取消踩
            removeDislikeStr = "取踩";
            dislikesMapper.deleteDislikeRecord(userId,likeArticleToBack.getTextType(),likeArticleToBack.getArticleId());
            if(likeArticleToBack.getTextType()==1){
                //article
                articleMapper.reduceDislike(likeArticleToBack.getArticleId());
            }
            else if(likeArticleToBack.getTextType()==2){
                //comment
                commentMapper.reduceDislike(likeArticleToBack.getArticleId());
            }
        }
        if(likeExist>0){
            //点过赞，取消赞
            likesMapper.deleteLikeRecord(userId,likeArticleToBack.getTextType(),likeArticleToBack.getArticleId());
            if(likeArticleToBack.getTextType()==1){
                //article
                articleMapper.reduceLike(likeArticleToBack.getArticleId());
                articleRedis.saveDataToCache(likeArticleToBack.getArticleId(),new ArticleInRedis(articleMapper.getArticleById(likeArticleToBack.getArticleId())));
            }
            else if(likeArticleToBack.getTextType()==2){
                //comment
                commentMapper.reduceLike(likeArticleToBack.getArticleId());
            }
            return ResponseMessage.success("成功取赞"+removeDislikeStr);
        }
        else{
            //没点过赞，点赞
            Likes tempLikes = new Likes(userId,likeArticleToBack.getTextType(),likeArticleToBack.getArticleId());
            likesMapper.addLikeRecord(tempLikes);
            if(likeArticleToBack.getTextType()==1){
                //article
                articleMapper.addLike(likeArticleToBack.getArticleId());
                articleRedis.saveDataToCache(likeArticleToBack.getArticleId(),new ArticleInRedis(articleMapper.getArticleById(likeArticleToBack.getArticleId())));
            }
            else if(likeArticleToBack.getTextType()==2){
                //comment
                commentMapper.addLike(likeArticleToBack.getArticleId());
            }
            return ResponseMessage.success("成功点赞"+removeDislikeStr);
        }

    }
}
