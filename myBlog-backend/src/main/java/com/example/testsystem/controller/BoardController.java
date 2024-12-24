package com.example.testsystem.controller;

import com.example.testsystem.model.Board;
import com.example.testsystem.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;
import java.util.List;

@RestController
@RequestMapping("/board")
public class BoardController {
    @Autowired
    BoardService boardService;

    @CrossOrigin(origins = "*")
    @PostMapping("/getAllByRoleId")
    public List<Board> getAllBoard(@RequestBody String roleId){
        return boardService.getAllBoardByRoleId(roleId);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/getAll")
    public List<Board> getAll(){
        return boardService.getAll();
    }
}
