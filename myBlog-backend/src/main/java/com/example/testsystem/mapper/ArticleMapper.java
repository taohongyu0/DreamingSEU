package com.example.testsystem.mapper;

import com.example.testsystem.model.Article;
import com.example.testsystem.model.supplement.ArticleInRankingList;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArticleMapper {
    @Insert("insert into article (title,author_id,content,create_time,modify_time,hits,likes,dislikes,allow_comment,board_id,cover) values (#{title},#{authorId},#{content},#{createTime},#{modifyTime},#{hits},#{likes},#{dislikes},#{allowComment},#{boardId},#{cover})")
    void insertArticle(Article article);

    @Delete("delete from article where article.id=#{articleId}")
    void deleteArticleById(int articleId);

    @Update("update article set article.likes = article.likes+1 where article.id=#{articleId}")
    void addLike(int articleId);

    @Update("update article set article.likes = article.likes-1 where article.id=#{articleId}")
    void reduceLike(int articleId);

    @Update("update article set article.dislikes = article.dislikes+1 where article.id=#{articleId}")
    void addDislike(int articleId);

    @Update("update article set article.dislikes = article.dislikes-1 where article.id=#{articleId}")
    void reduceDislike(int articleId);

    @Update("update article set article.hits=article.hits+1 where article.id=#{articleId}")
    void addHit(int articleId);

    @Update("update article set article.hits=article.hits-1 where article.id=#{articleId}")
    void reduceHit(int articleId);

    @Update("update article set article.author_id=-1 where article.author_id=#{authorId}")
    void updateAuthorIdTo_1(int authorId);

    @Select("select article.id,article.title,article.hits,article.cover from article where date(article.modify_time) >= date_sub(now(),interval 7 day) order by article.hits desc limit 10")
    List<ArticleInRankingList> getArticleRankingList();

    @Select("select article.id,article.title,article.hits,article.cover,article.board_id as boardId from article where article.cover is not null order by article.hits desc limit #{amount}")
    List<ArticleInRankingList> easyRecommend(int amount); //获取的文章数量

    @Select("select article.id,article.title,user.name as authorName,cover from article inner join user on article.author_id=user.id order by modify_time desc limit 100")
    List<Article> summaryView();

    @Select("select article.id,article.title,user.name as authorName,cover from article inner join user on article.author_id=user.id  where article.title like concat('%',#{keyWord},'%') order by modify_time desc limit 100")
    List<Article> search(String keyWord);

    @Select("select article.id,article.title,user.name as authorName,cover from article inner join user on article.author_id=user.id inner join board on article.board_id=board.id where board.name=#{boardName} order by modify_time desc limit 100")
    List<Article> getArticlesByBoardName(String boardName);

    @Select("select id,title,author_id as authorId,content,create_time as createTime,modify_time as modifyTime,hits,likes,dislikes,allow_comment as allowComment,board_id as boardId from article where article.id=#{id}")
    Article getArticleById(int id);

    @Select("select article.id,article.title,user.name as authorName from article inner join user on article.author_id=user.id where article.author_id=#{authorId} order by modify_time desc")
    List<Article> viewBlogByAuthorId(int authorId);

    @Select("select article.id,article.title,(select user.name from user where article.author_id=user.id) as authorName from article inner join collection on article.id=collection.article_id inner join user on collection.user_id=user.id where user.username=#{username} order by modify_time desc")
    List<Article> viewBlogByUserCollection(String username);

    @Select("select article.id,article.title,article.author_id,article.content,article.create_time,article.modify_time,article.hits,article.likes,article.dislikes,article.allow_comment,article.board_id,article.cover,(select count(collection.article_id) from collection where collection.article_id=article.id) as collect from article where article.author_id=#{authorId}")
    List<Article> getArticleByAuthorId(int authorId);

    @Select("select count(*) from comment where comment.article_id=#{articleId}")
    int getCommentAmountByArticleId(int articleId);
}
