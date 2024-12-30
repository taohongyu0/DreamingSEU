package com.example.testsystem.service;

import com.example.testsystem.Util.ResponseMessage;
import com.example.testsystem.model.Tipoff;

public interface TipoffService {
    ResponseMessage<String> addTipoff(Tipoff tipoff);
    ResponseMessage<String> solve(int id);
}
