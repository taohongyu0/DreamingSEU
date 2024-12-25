package com.example.testsystem.service.impl;

import com.example.testsystem.Util.ResponseMessage;
import com.example.testsystem.mapper.*;
import com.example.testsystem.model.*;
import com.example.testsystem.model.supplement.ArticleInRankingList;
import com.example.testsystem.model.toback.ArticleIdAndToken;
import com.example.testsystem.model.toback.ArticleToBack;
import com.example.testsystem.redis.ArticleRedis;
import com.example.testsystem.service.ArticleService;
import com.example.testsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class ArticleServiceImpl implements ArticleService {
    public String articleFolderPath = "../myblog-foreground/src/pictures/articleCovers";
    @Autowired
    ArticleMapper articleMapper;
    @Autowired
    TokenMapper tokenMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    HitsMapper hitsMapper;
    @Autowired
    LikesMapper likesMapper;
    @Autowired
    DislikesMapper dislikesMapper;
    @Autowired
    CollectionMapper collectionMapper;
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    BoardMapper boardMapper;
    @Autowired
    ArticleRedis articleRedis;

    @Override
    public ResponseMessage<String> createArticle(ArticleToBack articleToBack) {
        Token userToken = tokenMapper.getTokenByTokenStr(articleToBack.getToken());
        //先检查token是否有效
        if(userToken==null){
            return ResponseMessage.fail(500,"登录过期");
        }
        Article article = new Article(articleToBack.getTitle(), articleToBack.getContent(), userToken.getUserId(), articleToBack.getBoardId(),articleToBack.getCover());
        articleMapper.insertArticle(article);
        return ResponseMessage.success("成功");
    }

    @Override
    public ResponseMessage<String> deleteArticle(int articleId) {
        List<Comment> commentList = commentMapper.getCommentByArticleId(articleId);
        if(commentList!=null){
            for(int i=0;i<commentList.size();i++){
                likesMapper.deleteLikeRecordByTextTypeAndTextId(2,commentList.get(i).getId()); //删除评论对应的点赞记录
                dislikesMapper.deleteDislikeRecordByTextTypeAndTextId(2,commentList.get(i).getId()); //删除评论对应的点踩记录
            }
        }
        commentMapper.deleteCommentByArticleId(articleId); //删完评论对应点赞记录后，再删评论
        likesMapper.deleteLikeRecordByTextTypeAndTextId(1,articleId); //删文章点赞记录
        dislikesMapper.deleteDislikeRecordByTextTypeAndTextId(1,articleId); //删文章点踩记录
        articleMapper.deleteArticleById(articleId); //删除文章
        articleRedis.deleteKey(articleId); //在缓存中删除
        return ResponseMessage.success("成功");
    }

    @Override
    public ResponseMessage<String> addCoverPic(MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseMessage.fail(500,"wrong");
        }
        try {
            Random random = new Random();
            String randomNumber = String.valueOf(random.nextInt(10000));
            LocalDateTime nowLD = LocalDateTime.now();
            String now = String.valueOf(nowLD.getYear())+String.valueOf(nowLD.getMonth())+String.valueOf(nowLD.getDayOfMonth())+String.valueOf(nowLD.getHour())+String.valueOf(nowLD.getMinute())+String.valueOf(nowLD.getSecond())+String.valueOf(nowLD.getNano());
            String uniqueFileName = now +randomNumber+ ".png";
            // 创建上传目录（如果不存在）
            Path uploadPath = Paths.get("../myblog-foreground/src/pictures/articleCovers").toAbsolutePath().normalize();
            Files.createDirectories(uploadPath);
            // 保存文件到服务器
            Path filePath = uploadPath.resolve(uniqueFileName);
            Files.copy(file.getInputStream(), filePath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            return ResponseMessage.success(uniqueFileName);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseMessage.fail(500,"wrong");
        }
    }

    @Override
    public List<Article> summaryView() {
        return articleMapper.summaryView();
    }

    @Override
    public List<Article> viewByAuthorId(String token) {
        Token userToken = tokenMapper.getTokenByTokenStr(token);
        if(userToken==null){
            return new ArrayList<>();
        }
        return articleMapper.viewBlogByAuthorId(userToken.getUserId());
    }

    @Override
    public List<Article> viewByUserCollection(String username) {
        return articleMapper.viewBlogByUserCollection(username);
    }

    @Override
    public List<Article> searchArticle(String keyWord) {
        return articleMapper.search(keyWord);
    }

    @Override
    public List<Article> getArticleByBoardName(String boardName) {
        return articleMapper.getArticlesByBoardName(boardName);
    }

    @Override
    public List<ArticleInRankingList> getArticleRankingList() {
        List<ArticleInRankingList> rankingList = articleMapper.getArticleRankingList();
        for(int i=0;i<rankingList.size();i++){
            rankingList.get(i).setRank(i+1);
        }
        return rankingList;
    }

    @Override
    public List<ArticleInRankingList> recommend(String tokenStr) {
//        Token userToken = tokenMapper.getTokenByTokenStr(tokenStr);
//        if(userToken==null){
//            return new ArrayList<>();
//        }
        List<ArticleInRankingList> recommendList = articleMapper.easyRecommend();
        int counter=0;
        for(ArticleInRankingList article:recommendList){
            counter++;
            article.setRank(counter);
        }
        return recommendList;
    }

    @Override
    public Article viewAArticle(ArticleIdAndToken articleIdAndToken) {
        Token tempToken = tokenMapper.getTokenByTokenStr(articleIdAndToken.getTokenStr());
        int id = articleIdAndToken.getArticleId();
        Article tempArticle = articleRedis.ViewAArticle(id);
        //增加一个点击量
        articleMapper.addHit(id);

        //把评论放到文章中
        List<Comment>comments = commentMapper.getCommentByArticleIdAndViewerId(id,tempToken.getUserId());
        tempArticle.setComments(comments);

        User author = userMapper.getUserById(tempArticle.getAuthorId());
        tempArticle.setAuthorName(author.getName());

        //把收藏量放进去
        int collectionNum = collectionMapper.getCollectNumByArticleId(id);
        tempArticle.setCollect(collectionNum);

        //把是否点过赞、是否点过踩、是否收藏过放进去
        if(tempToken==null){
            tempArticle.setLikeExist(0);
            tempArticle.setDislikeExist(0);
            tempArticle.setCollectExist(0);
            return tempArticle;
        }
        else{
            int likeExist = likesMapper.recordExist(tempToken.getUserId(), 1,id);
            int dislikeExist = dislikesMapper.recordExist(tempToken.getUserId(), 1,id);
            int collectExist  = collectionMapper.recordExist(new Collection(tempToken.getUserId(), id));
            tempArticle.setLikeExist(likeExist);
            tempArticle.setDislikeExist(dislikeExist);
            tempArticle.setCollectExist(collectExist);
        }
        return tempArticle;
    }

    @Override
    public int coverExist(int articleId) {
        // 指定要检查的文件路径
        String filePath = articleFolderPath+"/"+articleId +".png"; // 替换为你的文件路径
        // 创建File对象
        File file = new File(filePath);
        // 判断文件是否存在
        if(file.exists()){
            return 1;
        }
        else{
            return 0;
        }
    }
}
