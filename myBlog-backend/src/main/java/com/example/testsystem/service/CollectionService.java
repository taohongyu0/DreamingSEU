package com.example.testsystem.service;

import com.example.testsystem.Util.ResponseMessage;
import com.example.testsystem.model.toback.CollectionToBack;

public interface CollectionService {
    ResponseMessage<String> addCollection(CollectionToBack collectionToBack);
    ResponseMessage<String> removeCollection(CollectionToBack collectionToBack);
    int articleCollectNum(int articleId);
}
