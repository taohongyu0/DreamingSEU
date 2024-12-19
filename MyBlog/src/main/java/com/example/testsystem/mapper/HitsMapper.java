package com.example.testsystem.mapper;

import com.example.testsystem.model.Hits;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface HitsMapper {
    @Insert("insert into hits (user_id,article_id,time) values (#{userId},#{articleId},#{time})")
    void addHit(Hits hits);

    @Select("select coalesce(sum(article.hits),0) from article where article.author_id=#{authorId}")
    int totalLaunchedArticleHitsByAuthorId(int authorId);
}
