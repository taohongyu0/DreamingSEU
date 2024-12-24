package com.example.testsystem.mapper;

import com.example.testsystem.model.Token;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TokenMapper {
    @Insert("insert into token (user_id,token_str,produce_time) values (#{userId},#{tokenStr},#{produceTime})")
    void addToken(Token token);

    @Delete("delete from token where token.token_str=#{tokenStr}")
    void deleteToken(String tokenStr);

    @Delete("delete from token where token.user_id=#{userId}")
    void deleteTokenByUserId(int userId);

    @Select("select id,user_id as userId, token_str as tokenStr, produce_time as produceTime from token where token_str=#{tokenStr}")
    Token getTokenByTokenStr(String tokenStr);
}
