package com.example.testsystem.exception;

import com.example.testsystem.Util.ResponseMessage;
import com.sun.net.httpserver.HttpServer;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandlerAdvice {

    Logger log = LoggerFactory.getLogger((GlobalExceptionHandlerAdvice.class));

    @ExceptionHandler({Exception.class}) //何种异常统一处理
    public ResponseMessage handlerException(Exception e, HttpServer request, HttpServletResponse response){
        log.error("统一异常：",e);
        return new ResponseMessage(500,"error",null);
    }

}
