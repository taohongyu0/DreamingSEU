package com.example.testsystem.redis;

import com.example.testsystem.mapper.ArticleMapper;
import com.example.testsystem.model.Article;
import com.example.testsystem.model.inRedis.ArticleInRedis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class ArticleRedis {
    @Autowired
    ArticleMapper articleMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    private static final Logger log = LoggerFactory.getLogger(ArticleRedis.class);

    public ArticleInRedis getDataFromCache(int key) {
        try {
            return (ArticleInRedis) redisTemplate.opsForValue().get(String.valueOf(key));
        } catch (Exception e) {
            log.error("Error retrieving data from cache for key {}", key, e);
            return null;
        }
    }

    public void saveDataToCache(int key, ArticleInRedis value) {
        try {
            redisTemplate.opsForValue().set(String.valueOf(key), value);
        } catch (Exception e) {
            log.error("Error saving data to cache for key {}", key, e);
        }
    }

    public void deleteKey(int key) {
        redisTemplate.delete(String.valueOf(key));
        // 如果你使用的是 StringRedisTemplate
        // stringRedisTemplate.delete(key);
    }

    public Article ViewAArticle(int id){
        //先在缓存里面看看有没有
        Article article = new Article();
        ArticleInRedis articleInRedis = getDataFromCache(id);
        if (articleInRedis == null) {
            System.out.println("from sql-database");
            log.info("Cache miss for user ID {}", id);
            try {
                article = articleMapper.getArticleById(id);
                if (article != null) {
                    //article转成articleInRedis
                    articleInRedis = new ArticleInRedis(article);
                    saveDataToCache(id, articleInRedis);
                }
            } catch (Exception e) {
                log.error("Error retrieving user from database for ID {}", id, e);
                // 可以选择抛出异常、返回null或返回一个默认值
            }
        }
        else{
            //在redis中找到
            article = new Article(articleInRedis);
            System.out.println("from redis");
            //redis中的点击量同步+1,并保存在redis中
            articleInRedis.setHits(articleInRedis.getHits()+1);
            saveDataToCache(id,articleInRedis);

        }
        //article = articleMapper.getArticleById(id);
        return article;
    }

    public void reduceHitInRedis(int articleId){
        ArticleInRedis articleInRedis = getDataFromCache(articleId);
        if(articleInRedis!=null){
            articleInRedis.setHits(articleInRedis.getHits()-1);
            saveDataToCache(articleId,articleInRedis);
        }
    }
}

