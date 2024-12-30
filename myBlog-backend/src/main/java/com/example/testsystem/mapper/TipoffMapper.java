package com.example.testsystem.mapper;

import com.example.testsystem.model.Tipoff;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface TipoffMapper {
    @Insert("insert into tipoff (user_id,text_type,text_id,time,content,is_solve) values (#{userId},#{textType},#{textId},#{time},#{content},#{isSolve})")
    void addTipoff(Tipoff tipoff);

    @Update("update tipoff set tipoff.is_solve=1 where tipoff.id=#{id}")
    void solveTipoff(int id);

    @Select("select tipoff.id,tipoff.user_id as userId,user.username,tipoff.text_type as textType,tipoff.text_id as textId,tipoff.time,tipoff.content,tipoff.is_solve as isSolve from tipoff inner join user on user.id=tipoff.user_id")
    List<Tipoff> viewAll();

    @Select("select tipoff.id,tipoff.user_id as userId,user.username,tipoff.text_type as textType,tipoff.text_id as textId,tipoff.time,tipoff.content,tipoff.is_solve as isSolve from tipoff inner join user on user.id=tipoff.user_id where tipoff.is_solve=0")
    List<Tipoff> viewAllUnsolved();
}
