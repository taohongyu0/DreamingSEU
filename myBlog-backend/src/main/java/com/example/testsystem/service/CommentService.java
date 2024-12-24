package com.example.testsystem.service;

import com.example.testsystem.Util.ResponseMessage;
import com.example.testsystem.model.Comment;
import com.example.testsystem.model.toback.CommentToBack;

public interface CommentService {
    ResponseMessage<String>launchComment(CommentToBack commentToBack);

    ResponseMessage<String>deleteComment(int commentId);

}
