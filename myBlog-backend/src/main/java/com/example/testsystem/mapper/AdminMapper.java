package com.example.testsystem.mapper;

import com.example.testsystem.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface AdminMapper {
    @Update("update user set user.banned=1 where user.id=#{userId}")
    void banUser(int userId);

    @Update("update user set user.banned=0 where user.id=#{userId}")
    void unbanUser(int userId);
    @Update("update article set article.allow_comment=0 where article.id=#{articleId}")
    void banArticleComment(int articleId);
    @Update("update article set article.allow_comment=1 where article.id=#{articleId}")
    void unbanArticleComment(int articleId);

    @Select("select id,username,name,email,banned from user where user.id=#{userId}")
    User getUserBriefInfoById(int userId);
    @Select("select id,username,name,email,banned from user where user.role_id=1")
    List<User> getAllUserBriefInfo();
}
