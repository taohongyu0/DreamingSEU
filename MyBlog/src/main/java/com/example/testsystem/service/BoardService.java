package com.example.testsystem.service;

import com.example.testsystem.model.Board;

import java.util.List;

public interface BoardService {
    List<Board> getAll();
    List<Board> getAllBoardByRoleId(String roleId);
}
