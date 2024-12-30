package com.example.testsystem.service;

import com.example.testsystem.Util.ResponseMessage;
import com.example.testsystem.model.Tipoff;

import java.util.List;

public interface TipoffService {
    ResponseMessage<String> addTipoff(Tipoff tipoff);
    ResponseMessage<String> solve(int id);
    List<Tipoff> viewAll();
    List<Tipoff> viewAllUnsolved();
}
