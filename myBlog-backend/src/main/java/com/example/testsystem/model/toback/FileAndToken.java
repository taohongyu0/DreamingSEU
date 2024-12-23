package com.example.testsystem.model.toback;

import org.springframework.web.multipart.MultipartFile;

public class FileAndToken {
    MultipartFile file;
    String token;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
