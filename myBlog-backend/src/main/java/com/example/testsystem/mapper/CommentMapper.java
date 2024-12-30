package com.example.testsystem.mapper;

import com.example.testsystem.model.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMapper {
    @Insert("insert into comment (author_id,article_id,content,launch_time,likes,dislikes) values (#{authorId},#{articleId},#{content},#{launchTime},#{likes},#{dislikes})")
    void addComment(Comment comment);

    @Delete("delete from comment where comment.article_id=#{articleId}")
    void deleteCommentByArticleId(int articleId);

    @Delete("delete from comment where comment.id = #{id}")
    void deleteCommentById(int id);

    @Update("update comment set comment.likes=comment.likes+1 where comment.id=#{id}")
    void addLike(int id);

    @Update("update comment set comment.likes=comment.likes-1 where comment.id=#{id}")
    void reduceLike(int id);

    @Update("update comment set comment.dislikes=comment.dislikes+1 where comment.id=#{id}")
    void addDislike(int id);

    @Update("update comment set comment.dislikes=comment.dislikes-1 where comment.id=#{id}")
    void reduceDislike(int id);

    @Update("update comment set comment.author_id=-1 where comment.author_id=#{authorId}")
    void updateAuthorIdTo_1(int authorId);

    @Select("select comment.id,comment.author_id as authorId,comment.article_id as articleId,comment.content,comment.launch_time as launchTime,comment.likes,comment.dislikes,user.name as authorName from comment inner join user on comment.author_id=user.id where comment.article_id=#{articleId}")
    List<Comment> getCommentByArticleId(int articleId);
    @Select("select comment.id,comment.author_id as authorId,comment.article_id as articleId,comment.content,comment.launch_time as launchTime,comment.likes,comment.dislikes,user.name as authorName,(select count(*) from likes where likes.user_id=#{viewerId} and likes.text_type=2 and likes.text_id=comment.id) as likeExist,(select count(*) from dislikes where dislikes.user_id=#{viewerId} and dislikes.text_type=2 and dislikes.text_id=comment.id) as dislikeExist from comment inner join user on comment.author_id=user.id where comment.article_id=#{articleId}")
    List<Comment> getCommentByArticleIdAndViewerId(int articleId,int viewerId);
    @Select("select comment.id,comment.author_id as authorId,comment.article_id as articleId,comment.content,comment.launch_time as launchTime,comment.likes,user.name as authorName,user.username as authorUsername from comment inner join user on comment.author_id=user.id where comment.id=#{commentId}")
    Comment getCommentByCommentId(int commentId);

}
