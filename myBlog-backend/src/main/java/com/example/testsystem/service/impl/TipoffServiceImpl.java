package com.example.testsystem.service.impl;

import com.example.testsystem.Util.ResponseMessage;
import com.example.testsystem.mapper.ArticleMapper;
import com.example.testsystem.mapper.CommentMapper;
import com.example.testsystem.mapper.TipoffMapper;
import com.example.testsystem.mapper.TokenMapper;
import com.example.testsystem.model.Article;
import com.example.testsystem.model.Comment;
import com.example.testsystem.model.Tipoff;
import com.example.testsystem.model.Token;
import com.example.testsystem.service.TipoffService;
import com.example.testsystem.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TipoffServiceImpl implements TipoffService {
    @Autowired
    TipoffMapper tipoffMapper;
    @Autowired
    TokenMapper tokenMapper;
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    ArticleMapper articleMapper;
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

    @Override
    public List<Tipoff> viewAll() {
        return tipoffMapper.viewAll();
    }

    @Override
    public List<Tipoff> viewAllUnsolved() {
        List<Tipoff> result = tipoffMapper.viewAllUnsolved();
        for(Tipoff t:result){
            if(t.getTextType()==2){  //如果举报的是评论
                Comment comment = commentMapper.getCommentByCommentId(t.getTextId());
                t.setCommentContent(comment.getContent());
                t.setArticleId(comment.getArticleId());
                t.setDefendantUserId(comment.getAuthorId());
                t.setDefendantUsername(comment.getAuthorUsername());
            }
            else if(t.getTextType()==1){  //如果举报的是博文
                t.setArticleId(t.getTextId());
            }
            else {
                return new ArrayList<>();
            }
            Article article = articleMapper.getArticleById(t.getArticleId());
            t.setArticleTitle(article.getTitle());
            if(t.getTextType()==1){
                t.setDefendantUserId(article.getAuthorId());
                t.setDefendantUsername(article.getAuthorUsername());
            }
        }
        return result;
    }
}
