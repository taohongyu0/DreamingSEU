package com.example.testsystem.service.impl;

import com.example.testsystem.Util.ResponseMessage;
import com.example.testsystem.mapper.AdminMapper;
import com.example.testsystem.mapper.ArticleMapper;
import com.example.testsystem.model.Article;
import com.example.testsystem.model.User;
import com.example.testsystem.model.inRedis.ArticleInRedis;
import com.example.testsystem.redis.ArticleRedis;
import com.example.testsystem.service.AdminService;
import com.example.testsystem.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    AdminMapper adminMapper;
    @Autowired
    ArticleMapper articleMapper;
    @Autowired
    ArticleService articleService;
    @Autowired
    ArticleRedis articleRedis;
    @Override
    public List<User> getAllUserBriefInfo() {
        return adminMapper.getAllUserBriefInfo();
    }

    @Override
    public ResponseMessage<String> banUser(int userId) {
        User tempUser = adminMapper.getUserBriefInfoById(userId);
        if(tempUser==null){
            return ResponseMessage.fail(500,"用户不存在");
        }
        if(tempUser.isBanned()){ //已经被封了，此时要解封
            adminMapper.unbanUser(userId);
            return ResponseMessage.success("成功解封");
        }
        else{ //没被封号，要封禁
            adminMapper.banUser(userId);
            return ResponseMessage.success("成功封禁");
        }
    }

    @Override
    public ResponseMessage<String> banArticleComment(int articleId) {
        Article article = articleMapper.getArticleById(articleId);
        if(article.isAllowComment()){
            adminMapper.banArticleComment(articleId);
            article.setAllowComment(false);
            articleRedis.saveDataToCache(articleId,new ArticleInRedis(article));
            return ResponseMessage.success("成功关闭评论区");
        }
        else{
            adminMapper.unbanArticleComment(articleId);
            article.setAllowComment(true);
            articleRedis.saveDataToCache(articleId,new ArticleInRedis(article));
            return ResponseMessage.success("成功打开评论区");
        }
    }

    @Override
    public ResponseMessage<String> clearPic() {
        List<String> allArticleCover = articleMapper.getAllArticleCover();
        List<String> allPic = new ArrayList<>();
        List<String> delPic = new ArrayList<>();
        //获取存articleCover文件夹中的所有文件名
        ArticleServiceImpl a = new ArticleServiceImpl();
        String coverPath =  a.articleFolderPath;
        File dir = new File(coverPath);
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) { // 检查是否是文件
                    allPic.add(file.getName());
                }
            }
        } else {
            return ResponseMessage.fail(500,"目录不存在或无法读取");
        }

        //看看allCoverPic中有哪些是allArticleCover中没出现的，就把改文件名加到要删除的那个列表里
        for(String picInAllPic:allPic){
            boolean isFind = false;
            for(String picInAllArticleCover:allArticleCover){
                if(picInAllPic.equals(picInAllArticleCover)){
                    isFind=true;
                    break;
                }
            }
            if(!isFind){
                delPic.add(picInAllPic);
            }
        }

        for(String aDelPic:delPic){
            File file = new File(coverPath+"/"+aDelPic);
            if(!file.delete()){
                return ResponseMessage.fail(500,aDelPic+"文件删除失败");
            }
        }
        return ResponseMessage.success("success");
    }
}
