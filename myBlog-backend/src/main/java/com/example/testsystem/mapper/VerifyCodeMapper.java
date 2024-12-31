package com.example.testsystem.mapper;

import com.example.testsystem.model.VerifyCode;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface VerifyCodeMapper {
    @Insert("insert into verify_code (email,code,produce_time,valid) values (#{email},#{code},#{produceTime},1)")
    void add(VerifyCode verifyCode);

    @Delete("delete from verify_code where verify_code.email=#{email}")
    void deleteByEmail(String email);

    @Update("update verify_code set verify_code.valid=0 where verify_code.email=#{email}")
    void setInvalidByEmail(String email);

    @Select("select verify_code.id,verify_code.email,verify_code.code,verify_code.produce_time as produceTime,verify_code.valid from verify_code where verify_code.email=#{email} and verify_code.code=#{code} order by verify_code.produce_time desc limit 1")
    VerifyCode get(String email,String code);

    @Select("select verify_code.id,verify_code.email,verify_code.code,verify_code.produce_time as produceTime,verify_code.valid from verify_code where verify_code.email=#{email} order by verify_code.produce_time desc limit 1")
    VerifyCode getByEmail(String email);

}
