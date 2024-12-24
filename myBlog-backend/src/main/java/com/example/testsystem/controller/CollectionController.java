package com.example.testsystem.controller;

import com.example.testsystem.Util.ResponseMessage;
import com.example.testsystem.model.Article;
import com.example.testsystem.model.toback.CollectionToBack;
import com.example.testsystem.service.CollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/collect")
public class CollectionController {
    @Autowired
    CollectionService collectionService;

    @CrossOrigin(origins = "*")
    @PostMapping("/add")
    public ResponseMessage<String>addCollect(@RequestBody CollectionToBack collectionToBack){
        return collectionService.addCollection(collectionToBack);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/remove")
    public ResponseMessage<String>removeCollect(@RequestBody CollectionToBack collectionToBack){
        return collectionService.removeCollection(collectionToBack);
    }
}
