package com.example.testsystem.service.impl;

import com.example.testsystem.Util.ResponseMessage;
import com.example.testsystem.mapper.ArticleMapper;
import com.example.testsystem.mapper.CommentMapper;
import com.example.testsystem.mapper.HitsMapper;
import com.example.testsystem.mapper.TokenMapper;
import com.example.testsystem.model.Comment;
import com.example.testsystem.model.Token;
import com.example.testsystem.model.toback.CommentToBack;
import com.example.testsystem.redis.ArticleRedis;
import com.example.testsystem.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    TokenMapper tokenMapper;
    @Autowired
    ArticleMapper articleMapper;
    @Autowired
    HitsMapper hitsMapper;
    @Autowired
    ArticleRedis articleRedis;
    @Override
    public ResponseMessage<String> launchComment(CommentToBack commentToBack) {
        //先获取发评论人的id
        Token tempToken = tokenMapper.getTokenByTokenStr(commentToBack.getToken());
        if(tempToken == null){
            return ResponseMessage.fail(500,"token无效");
        }
        int userId = tempToken.getUserId();
        Comment tempComment = new Comment(userId, commentToBack.getArticleId(), commentToBack.getContent());
        commentMapper.addComment(tempComment);

        //修bug（点击量+1的bug）
        articleMapper.reduceHit(commentToBack.getArticleId());
        articleRedis.reduceHitInRedis(commentToBack.getArticleId());
        return ResponseMessage.success("成功");
    }

    @Override
    public ResponseMessage<String> deleteComment(int commentId) {
        Comment tempComment = commentMapper.getCommentByCommentId(commentId);

        //修bug（点击量+1的bug）
        articleMapper.reduceHit(tempComment.getArticleId());
        articleRedis.reduceHitInRedis(tempComment.getArticleId());

        commentMapper.deleteCommentById(commentId);
        return ResponseMessage.success("成功");
    }
}
