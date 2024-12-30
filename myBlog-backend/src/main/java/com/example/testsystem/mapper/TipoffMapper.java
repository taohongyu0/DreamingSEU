package com.example.testsystem.mapper;

import com.example.testsystem.model.Tipoff;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface TipoffMapper {
    @Insert("insert into tipoff (user_id,text_type,text_id,time,content,is_solve) values (#{userId},#{textType},#{textId},#{time},#{content},#{isSolve})")
    void addTipoff(Tipoff tipoff);

    @Update("update tipoff set tipoff.is_solve=1 where tipoff.id=#{id}")
    void solveTipoff(int id);
}
