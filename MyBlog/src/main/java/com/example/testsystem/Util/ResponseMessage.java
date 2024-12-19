package com.example.testsystem.Util;

import org.springframework.http.HttpStatus;

public class ResponseMessage<T> {
    int code;
    String message;
    T data;

    public ResponseMessage(){}

    public ResponseMessage(int i, String error, Object o) {
    }

    public static <T> ResponseMessage<T> success(T data){
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setCode(HttpStatus.OK.value());
        responseMessage.setMessage("success");
        responseMessage.setData(data);
        return responseMessage;
    }

    public static <T> ResponseMessage<T> fail(int code,String message){
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setCode(code);
        responseMessage.setMessage(message);
        responseMessage.setData(null);
        return responseMessage;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
