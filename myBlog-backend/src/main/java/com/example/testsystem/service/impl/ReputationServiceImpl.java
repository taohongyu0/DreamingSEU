package com.example.testsystem.service.impl;

import com.example.testsystem.mapper.ArticleMapper;
import com.example.testsystem.mapper.DislikesMapper;
import com.example.testsystem.mapper.LikesMapper;
import com.example.testsystem.model.Article;
import com.example.testsystem.service.ReputationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReputationServiceImpl implements ReputationService {
    @Autowired
    ArticleMapper articleMapper;
    @Autowired
    LikesMapper likesMapper;
    @Autowired
    DislikesMapper dislikesMapper;
    @Override
    public double calByUserId(int userId) {
        List<Article> userArticles = articleMapper.getArticleByAuthorId(userId); //获取该用户发表的所有文章
        double articleReputation = 0; //所有文章提供的声望值
        for(Article article:userArticles){
            double likeRate = 1; //点赞率
            if(article.getLikes()>10){
                likeRate = (double) (article.getLikes() /(article.getLikes()+article.getDislikes()))+1;
            }
            int commentAmount = articleMapper.getCommentAmountByArticleId(article.getId());
            articleReputation = articleReputation + 10 + article.getLikes()*likeRate + article.getCollect()*10 + article.getHits()*0.2 + commentAmount*0.5;
            if(article.getHits()>20){
                articleReputation = articleReputation - article.getDislikes()*2;
            }
        }
        int commentLikes = likesMapper.totalLaunchedCommentLikesByAuthorId(userId);
        int commentDislikes = dislikesMapper.totalLaunchedCommentLikesByAuthorId(userId);
        return 100+articleReputation+commentLikes*0.5-commentDislikes*0.5;
    }
}
