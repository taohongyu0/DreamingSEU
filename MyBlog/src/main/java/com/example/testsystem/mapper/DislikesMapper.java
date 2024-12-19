package com.example.testsystem.mapper;

import com.example.testsystem.model.Likes;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DislikesMapper {
    @Insert("insert into dislikes (user_id,text_type,text_id,time) values (#{userId},#{textType},#{textId},#{time})")
    void addDislikeRecord(Likes likes);

    @Delete("delete from dislikes where user_id=#{userId} and text_type=#{textType} and text_id=#{textId}")
    void deleteDislikeRecord(int userId,int textType,int textId);

    @Delete("delete from dislikes where dislikes.text_type=#{textType} and dislikes.text_id=#{textId}")
    void deleteDislikeRecordByTextTypeAndTextId(int textType,int textId);

    @Delete("delete from dislikes where dislikes.user_id=#{userId}")
    void deleteDislikeRecordByUserId(int userId);

    @Select("select count(*) from dislikes where dislikes.user_id=#{userId} and dislikes.text_type=#{textType} and dislikes.text_id=#{textId}")
    int recordExist(int userId,int textType,int textId);

    @Select("select coalesce(sum(article.dislikes),0) from article where article.author_id=#{authorId}")
    int totalLaunchedArticleDislikesByAuthorId(int authorId);  //从已发表的文章中收到的所有点踩总数

    @Select("select coalesce(sum(comment.dislikes),0) from comment where comment.author_id=#{authorId}")
    int totalLaunchedCommentLikesByAuthorId(int authorId);  //从已发表的评论中收到的所有点踩总数
}
