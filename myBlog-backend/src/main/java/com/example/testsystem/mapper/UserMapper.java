package com.example.testsystem.mapper;

import com.example.testsystem.model.User;
import com.example.testsystem.model.supplement.PersonalCenterInfo;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    @Insert("insert into user (username,name,password_hash,email,role_id,banned) values (#{username},#{name},#{passwordHash},#{email},#{roleId},#{banned})")
    void insertUser(User user);

    @Delete("delete from user where user.username=#{username}")
    void deleteUserByUsername(String username);

    @Delete("delete from user where user.id=#{userId}")
    void deleteUserById(int userId);

    @Update("update user set user.password_hash=#{passwordHash} where user.username=#{username}")
    void updatePassword(User user);

    @Select("select count(*) from user where username=#{username}")
    int usernameExist(String username);

    @Select("select count(*) from user where user.username=#{username} and user.password_hash=#{passwordHash}")
    int verify(User user);

    @Select("select count(*) from user where user.username=#{username} and user.email=#{email}")
    int verifyUsernameAndEmail(User user);

    @Select("select id,username,name,password_hash as passwordHash,email,role_id as roleId,banned from user where user.username=#{username}")
    User getUserByUsername(String username);

    @Select("select id,username,name,password_hash as passwordHash,email from user where user.id=#{id}")
    User getUserById(int id);

    @Select("select user.id,user.username,user.name,token.produce_time as loginTime from user inner join token on user.id=token.user_id where token.token_str=#{tokenStr}")
    PersonalCenterInfo getCenterInfoByTokenStr(String tokenStr);

    @Select("select user.id,user.username,user.name from user where user.id=#{userId}")
    PersonalCenterInfo getCenterInfoByUserId(int userId);
}
