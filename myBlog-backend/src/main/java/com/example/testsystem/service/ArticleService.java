package com.example.testsystem.service;


import com.example.testsystem.Util.ResponseMessage;
import com.example.testsystem.model.Article;
import com.example.testsystem.model.supplement.ArticleInRankingList;
import com.example.testsystem.model.toback.ArticleIdAndToken;
import com.example.testsystem.model.toback.ArticleToBack;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

public interface ArticleService {
    ResponseMessage<String> createArticle(ArticleToBack articleToBack);
    ResponseMessage<String> deleteArticle(int articleId);
    ResponseMessage<String> addCoverPic(MultipartFile file);
    List<Article> summaryView();
    List<Article> viewByAuthorId(String token);
    List<Article> searchArticle(String keyWord);
    List<Article> getArticleByBoardName(String boardName);
    List<ArticleInRankingList> getArticleRankingList();
    Article viewAArticle(ArticleIdAndToken articleIdAndToken);
    int coverExist(int articleId); //判断文章封面是否存在
}
