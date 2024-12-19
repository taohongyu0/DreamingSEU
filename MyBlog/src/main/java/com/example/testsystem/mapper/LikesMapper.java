package com.example.testsystem.mapper;

import com.example.testsystem.model.Likes;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LikesMapper {
    @Insert("insert into likes (user_id,text_type,text_id,time) values (#{userId},#{textType},#{textId},#{time})")
    void addLikeRecord(Likes likes);

    @Delete("delete from likes where user_id=#{userId} and text_type=#{textType} and text_id=#{textId}")
    void deleteLikeRecord(int userId,int textType,int textId);

    @Delete("delete from likes where likes.text_type=#{textType} and likes.text_id=#{textId}")
    void deleteLikeRecordByTextTypeAndTextId(int textType,int textId);

    @Delete("delete from likes where likes.user_id=#{userId}")
    void deleteLikeRecordByUserId(int userId);

    @Select("select count(*) from likes where likes.user_id=#{userId} and likes.text_type=#{textType} and likes.text_id=#{textId}")
    int recordExist(int userId,int textType,int textId);

    @Select("select coalesce(sum(article.likes),0) from article where article.author_id=#{authorId}")
    int totalLaunchedArticleLikesByAuthorId(int authorId);  //从已发表的文章中收到的所有点赞总数

    @Select("select coalesce(sum(comment.likes),0) from comment where comment.author_id=#{authorId}")
    int totalLaunchedCommentLikesByAuthorId(int authorId);  //从已发表的评论中收到的所有点赞总数
}
