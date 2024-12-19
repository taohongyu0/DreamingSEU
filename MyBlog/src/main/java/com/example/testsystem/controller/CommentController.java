package com.example.testsystem.controller;

import com.example.testsystem.Util.ResponseMessage;
import com.example.testsystem.model.toback.CommentToBack;
import com.example.testsystem.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    CommentService commentService;

    @CrossOrigin("*")
    @PostMapping("/launch")
    public ResponseMessage<String> launchComment(@RequestBody CommentToBack commentToBack){
        return commentService.launchComment(commentToBack);
    }

    @CrossOrigin("*")
    @PostMapping("/delete")
    public ResponseMessage<String> deleteComment(@RequestBody int commentId){
        return commentService.deleteComment(commentId);
    }
}
