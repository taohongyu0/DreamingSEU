package com.example.testsystem.mapper;

import com.example.testsystem.model.Collection;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CollectionMapper {
    @Insert("insert into collection (article_id,user_id) values (#{articleId},#{userId})")
    void addCollection(Collection collection);

    @Delete("delete from collection where collection.article_id=#{articleId} and collection.user_id=#{userId}")
    void removeCollection(Collection collection);

    @Delete("delete from collection where collection.user_id=#{userId}")
    void removeByUserId(int userId);

    @Select("select count(*) from collection where collection.article_id=#{articleId}")
    int getCollectNumByArticleId(int articleId);

    @Select("select count(*) from collection where collection.article_id=#{articleId} and collection.user_id=#{userId}")
    int recordExist(Collection collection);
}
