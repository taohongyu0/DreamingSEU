package com.example.testsystem.Util;

import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Properties;
import java.util.Random;

public class Mail {
    public Mail(String e_mail) throws MessagingException, GeneralSecurityException, UnsupportedEncodingException {  //传参为要接收验证码的邮箱
        this.email = e_mail;
        GenerateCode(6);
        SendEmail();
    }

    public void GenerateCode(int figuresNum) {
        Random random = new Random();
        String tempCode = "";
        for (int i = 0; i < figuresNum; i++) {
            int tempFigure = random.nextInt(10);
            tempCode += String.valueOf(tempFigure);
        }
        this.verifiCode = tempCode;
    }

    private String verifiCode;
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVerifiCode() {
        return verifiCode;
    }

    public void setVerifiCode(String verifiCode) {
        this.verifiCode = verifiCode;
    }

    public void SendEmail() throws MessagingException, GeneralSecurityException, UnsupportedEncodingException {
        //创建一个配置文件并保存
        Properties properties = new Properties();

        properties.setProperty("mail.smtp.host", "smtp.qq.com");

        properties.setProperty("mail.transport.protocol", "smtp");

        properties.setProperty("mail.smtp.auth", "true");


        //QQ存在一个特性设置SSL加密
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.ssl.socketFactory", sf);

        //创建一个session对象
        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("3341213658@qq.com", "cgmvmfxzultwdbfi");
            }
        });

        //开启debug模式
        session.setDebug(false);

        //获取连接对象
        Transport transport = session.getTransport();

        //连接服务器
        transport.connect("smtp.qq.com", 465,"3341213658@qq.com", "cgmvmfxzultwdbfi");

        //创建邮件对象
        MimeMessage mimeMessage = new MimeMessage(session);

        //邮件发送人
        mimeMessage.setFrom(new InternetAddress("3341213658@qq.com"));

        //邮件接收人
        mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(this.email));

        //邮件标题
        mimeMessage.setSubject("【吹梦到东南】 验证码");

        String content = "【吹梦到东南】您好！欢迎使用“吹梦到东南”博客系统！您的验证码是：" + this.verifiCode + "。该验证码仅在10分钟内有效，请尽快完成验证。如非本人操作，请检查账号安全！";

        //邮件内容
        mimeMessage.setContent(content, "text/html;charset=UTF-8");

        //发送邮件
        transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());

        //关闭连接
        transport.close();
    }



}

