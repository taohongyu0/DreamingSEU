package com.example.testsystem.service.impl;

import com.example.testsystem.mapper.BoardMapper;
import com.example.testsystem.model.Board;
import com.example.testsystem.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardServiceImpl implements BoardService {
    @Autowired
    BoardMapper boardMapper;

    @Override
    public List<Board> getAll() {
        return boardMapper.getAll();
    }

    @Override
    public List<Board> getAllBoardByRoleId(String roleId) {
        return boardMapper.getAllBoardByRoleId(Integer.parseInt(roleId));
    }
}
