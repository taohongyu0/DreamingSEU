package com.example.testsystem.controller;

import com.example.testsystem.Util.ResponseMessage;
import com.example.testsystem.model.Article;
import com.example.testsystem.model.supplement.ArticleInRankingList;
import com.example.testsystem.model.toback.ArticleIdAndToken;
import com.example.testsystem.model.toback.ArticleToBack;
import com.example.testsystem.model.toback.LikeArticleToBack;
import com.example.testsystem.service.ArticleService;
import com.example.testsystem.service.DislikesService;
import com.example.testsystem.service.LikesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    ArticleService articleService;
    @Autowired
    LikesService likesService;
    @Autowired
    DislikesService dislikesService;

    @CrossOrigin(origins = "*")
    @PostMapping("/createArticle")
    public ResponseMessage<String> createArticle(@RequestBody ArticleToBack articleToBack){
        return articleService.createArticle(articleToBack);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/addCoverPic")
    public ResponseMessage<String> addCoverPic(@RequestParam("file") MultipartFile file){
        return articleService.addCoverPic(file);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/deleteArticle")
    public ResponseMessage<String> deleteArticle(@RequestBody int articleId){
        return articleService.deleteArticle(articleId);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/blogPush")
    public List<Article> blogPush(){
        return articleService.summaryView();
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/myBlog")
    public List<Article> myBlog(@RequestBody String token){
        return articleService.viewByAuthorId(token);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/myCollection")
    public List<Article> myCollection(@RequestBody String username){
        return articleService.viewByUserCollection(username);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/blogPushNull")
    public List<Article> blogPushNull(){
        return new ArrayList<>();
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/blogSearch")
    public List<Article> blogSearch(@RequestBody String keyWord){
        return articleService.searchArticle(keyWord);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/boardArticle")
    public List<Article> boardArticle(@RequestBody String boardName){
        return articleService.getArticleByBoardName(boardName);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/rankingList")
    public List<ArticleInRankingList> getArticleRankingList(){
        return articleService.getArticleRankingList();
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/view")
    public Article viewAArticle(@RequestBody ArticleIdAndToken articleIdAndToken){
        return articleService.viewAArticle(articleIdAndToken);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/likes") //不管是点赞文章还是点赞评论，都用的这个接口
    public ResponseMessage<String> likeArticle(@RequestBody LikeArticleToBack likeArticleToBack){
        return likesService.likeArticle(likeArticleToBack);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/dislikes")
    public ResponseMessage<String> dislikeArticle(@RequestBody LikeArticleToBack likeArticleToBack){
        return dislikesService.dislikeArticle(likeArticleToBack);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/coverExist")
    public int coverExist(@RequestBody String articleId){
        return articleService.coverExist(Integer.parseInt(articleId));
    }
}
