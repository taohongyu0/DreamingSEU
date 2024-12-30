package com.example.testsystem.controller;

import com.example.testsystem.Util.ResponseMessage;
import com.example.testsystem.model.Tipoff;
import com.example.testsystem.service.TipoffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tipoff")
public class TipoffController {
    @Autowired
    TipoffService tipoffService;

    @CrossOrigin(origins = "*")
    @PostMapping("/add")
    ResponseMessage<String> add(@RequestBody Tipoff tipoff){
        return tipoffService.addTipoff(tipoff);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/solve")
    ResponseMessage<String> solve(@RequestBody String id){
        return tipoffService.solve(Integer.parseInt(id));
    }
}
