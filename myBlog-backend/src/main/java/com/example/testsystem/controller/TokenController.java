package com.example.testsystem.controller;

import com.example.testsystem.Util.ResponseMessage;
import com.example.testsystem.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/token")
public class TokenController {
    @Autowired
    TokenService tokenService;

    @CrossOrigin("*")
    @PostMapping("/getUserId")
    ResponseMessage<Integer> getUserId(@RequestBody String token){
        return tokenService.getUserIdByToken(token);
    }
}
