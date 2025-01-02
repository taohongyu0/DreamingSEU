package com.example.testsystem.service.impl;

import com.example.testsystem.Util.ResponseMessage;
import com.example.testsystem.mapper.*;
import com.example.testsystem.model.*;
import com.example.testsystem.model.Collection;
import com.example.testsystem.model.supplement.ArticleInRankingList;
import com.example.testsystem.model.supplement.BoardIdAndCount;
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
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

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
    @Autowired
    UserService userService;

    @Override
    public ResponseMessage<String> createArticle(ArticleToBack articleToBack) {
        if(articleToBack.getContent()==null || Objects.equals(articleToBack.getContent(), "")){
            return ResponseMessage.fail(500,"不能发表内容为空的博文！");
        }
        if(articleToBack.getBoardId()==0){
            return ResponseMessage.fail(500,"请选择一个板块！");
        }
        if(articleToBack.getContent().length()>21000){
            return ResponseMessage.fail(500,"字数超出最大允许值，服务器拒绝保存！");
        }
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
        List<ArticleInRankingList> finalRecommendList = articleMapper.easyRecommend(10);
        Token userToken = tokenMapper.getTokenByTokenStr(tokenStr);
        if(userToken!=null){
            //先计算该用户喜欢的板块
            List<Integer>userLikesBoard = boardMapper.getUserLikesBoardId(userToken.getUserId());
            List<BoardIdAndCount>userLikesBoardCount = new ArrayList<>();
            for (Integer integer : userLikesBoard) {
                if(integer==null){
                    continue;
                }
                boolean isFind = false;
                for (BoardIdAndCount boardIdAndCount : userLikesBoardCount) { //先在count数组里找一遍
                    if (integer.equals(boardIdAndCount.getBoardId())) {
                        isFind = true;
                        boardIdAndCount.setCount(boardIdAndCount.getCount()+1);
                        break;
                    }
                }
                if (!isFind) {
                    BoardIdAndCount tempBoardAndCount = new BoardIdAndCount();
                    tempBoardAndCount.setBoardId(integer);
                    tempBoardAndCount.setCount(1);
                    userLikesBoardCount.add(tempBoardAndCount);
                }
            }
            //如果用户没有点赞记录，直接简单推荐；如果有，按照用户喜好推荐
            if(!userLikesBoardCount.isEmpty()){
                //给用户最喜欢的板块排序（升序排）
                userLikesBoardCount.sort(new Comparator<BoardIdAndCount>() {
                    @Override
                    public int compare(BoardIdAndCount o1, BoardIdAndCount o2) {
                        return Integer.compare(o1.getCount(), o2.getCount());
                    }
                });
                //按照n+1（用户点赞量倒数第几，n就是几）乘点击量得到推荐系数，推荐系数越大，越排前面
                List<ArticleInRankingList> recommendList = articleMapper.easyRecommend(1000);
                for(ArticleInRankingList article:recommendList){
                    for(int i=0;i<userLikesBoardCount.size();i++){
                        if(article.getBoardId()==userLikesBoardCount.get(i).getBoardId()){
                            article.setHits(article.getHits()*(i+2));
                            break;
                        }
                    }
                }
                //按照时间（修改日期到现在过了多少天，时间越短乘的系数越高）
                for(ArticleInRankingList article:recommendList){
                    Duration duration = Duration.between(article.getModifyTime(),LocalDateTime.now());
                    long day = duration.toDays();
                    if(day<=1){
                        article.setHits(article.getHits()*10);
                    }
                    else if(day<=3){
                        article.setHits(article.getHits()*6);
                    }
                    else if(day<=7){
                        article.setHits(article.getHits()*3);
                    }
                    else if(day<=30){
                        article.setHits(article.getHits()*2);
                    }
                }
                //对tempRank再按照推荐系数排序(降序)
                recommendList.sort(new Comparator<ArticleInRankingList>() {
                    @Override
                    public int compare(ArticleInRankingList o1, ArticleInRankingList o2) {
                        return Integer.compare(o2.getHits(),o1.getHits());
                    }
                });
                finalRecommendList = recommendList;
            }
        }
        int counter=0;
        for(ArticleInRankingList article:finalRecommendList){
            counter++;
            article.setRank(counter);
        }
        return finalRecommendList;
    }

    @Override
    public Article viewAArticle(ArticleIdAndToken articleIdAndToken) {
        Token tempToken = tokenMapper.getTokenByTokenStr(articleIdAndToken.getTokenStr());
        int id = articleIdAndToken.getArticleId();
        Article tempArticle = articleRedis.ViewAArticle(id);
        //增加一个点击量
        articleMapper.addHit(id);

        //把作者头像放进去
        tempArticle.setAuthorProfile(userService.getUserProfile(tempArticle.getAuthorId()));

        //把评论放到文章中
        List<Comment>comments = commentMapper.getCommentByArticleIdAndViewerId(id,tempToken.getUserId());
        for(Comment comment:comments){
            comment.setAuthorProfile(userService.getUserProfile(comment.getAuthorId()));
        }
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
