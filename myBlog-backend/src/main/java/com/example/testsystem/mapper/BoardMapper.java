package com.example.testsystem.mapper;

import com.example.testsystem.model.Board;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BoardMapper {
    @Select("select * from board inner join board_role_relation on board.id=board_role_relation.board_id where board_role_relation.role_id=#{roleId}")
    List<Board> getAllBoardByRoleId(int roleId);
    @Select("select * from board")
    List<Board> getAll();

    @Select("select * from board where board.name=#{name}")
    Board getBoardByName(String name);

    @Select("select article.board_id from likes inner join article on likes.text_id=article.id where likes.text_type=1 and likes.user_id=#{userId}")
    List<Integer> getUserLikesBoardId(int userId);
}
