package com.example.testsystem.Util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;

public class MD5Example {

    public String getMD5Hash(String input) {
        if(input==null || input.isEmpty()){
            return "";
        }
        try {
            // 获取MD5算法的MessageDigest实例  
            MessageDigest md = MessageDigest.getInstance("MD5");

            // 将输入字符串转换为字节数组并计算哈希值  
            byte[] messageDigest = md.digest(input.getBytes());

            // 将字节数组转换为十六进制字符串  
            BigInteger no = new BigInteger(1, messageDigest);

            // 去掉前导零并转换为小写  
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

//    public static void main(String[] args) {
//        String input = "Hello, World!";
//        String md5Hash = getMD5Hash(input);
//        System.out.println("MD5 Hash of '" + input + "' is: " + md5Hash);
//    }
}