package com.example.testsystem.service.impl;

import com.example.testsystem.Util.MD5Example;
import com.example.testsystem.Util.Mail;
import com.example.testsystem.Util.ResponseMessage;
import com.example.testsystem.mapper.*;
import com.example.testsystem.model.Article;
import com.example.testsystem.model.Token;
import com.example.testsystem.model.User;
import com.example.testsystem.model.VerifyCode;
import com.example.testsystem.model.supplement.PersonalCenterInfo;
import com.example.testsystem.model.toback.RoleIdAndToken;
import com.example.testsystem.redis.ArticleRedis;
import com.example.testsystem.service.ReputationService;
import com.example.testsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    TokenMapper tokenMapper;
    @Autowired
    HitsMapper hitsMapper;
    @Autowired
    LikesMapper likesMapper;
    @Autowired
    DislikesMapper dislikesMapper;
    @Autowired
    CollectionMapper collectionMapper;
    @Autowired
    ArticleMapper articleMapper;
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    ArticleRedis articleRedis;
    @Autowired
    ReputationService reputationService;
    @Autowired
    VerifyCodeMapper verifyCodeMapper;
//    public String UPLOADED_FOLDER = "F:\\Grade4Term1\\Java\\MyBlog-2\\myblog-foreground\\src\\pictures\\userProfiles";
    public String UPLOADED_FOLDER = "../myblog-foreground/src/pictures/userProfiles";

    MD5Example md5 = new MD5Example();

    @Override
    public ResponseMessage<String> register(User user) {
        //先检查用户名是否重复
        int isExist = userMapper.usernameExist(user.getUsername());
        if(isExist > 0){  //重复，返回用户名已被注册
            return ResponseMessage.fail(500,"用户名已被注册");
        }

        String passwordHash = md5.getMD5Hash(user.getPasswordHash());
        user.setPasswordHash(passwordHash);
        user.setRoleId(1); //1为普通用户
        user.setBanned(false); //false为未封禁（正常）状态
        userMapper.insertUser(user);
        return ResponseMessage.success("成功");
    }

    @Override
    public ResponseMessage<RoleIdAndToken> login(User user) {
        user.setPasswordHash(md5.getMD5Hash(user.getPasswordHash()));
        int isRight = userMapper.verify(user);
        if(isRight == 0){ //用户名或密码错误
            return ResponseMessage.fail(500,"用户名或密码错误");
        }
        //密码正确
        user = userMapper.getUserByUsername(user.getUsername());
        //先删除该用户之前登录的所有token记录
        tokenMapper.deleteTokenByUserId(user.getId());
        // 使用当前时间戳作为种子
        long seed = System.currentTimeMillis();
        Random random = new Random(seed);
        // 生成随机数
        int randomInt = random.nextInt();
        //获取当前时间
        LocalDateTime now = LocalDateTime.now();
        String token = md5.getMD5Hash(Integer.toString(randomInt) + user.getUsername() + now); //token为随机数+用户名+当前时间组合之后再用MD5加密
        //把该用户的token存到后台数据库
        Token newToken = new Token(user.getId(), token);
        tokenMapper.addToken(newToken);
        if(user.getRoleId()==2){ //管理员
            return ResponseMessage.success(new RoleIdAndToken(2,token, user.isBanned()));
        }
        else{  //普通用户
            return ResponseMessage.success(new RoleIdAndToken(1,token, user.isBanned()));
        }

    }

    @Override
    public ResponseMessage<String> delUser(int userId) {
        //删除redis缓存中article的数据
        List<Article> userBlog = articleMapper.viewBlogByAuthorId(userId);
        for (Article article : userBlog) {
            articleRedis.deleteKey(article.getId());
        }
        //再删除数据库中相关数据
        //先删用户的收藏夹
        collectionMapper.removeByUserId(userId);
        //再删用户点赞、点踩记录
        likesMapper.deleteLikeRecordByUserId(userId);
        dislikesMapper.deleteDislikeRecordByUserId(userId);
        //再删用户的token记录
        tokenMapper.deleteTokenByUserId(userId);
        //再把article和comment的author_id变成-1
        articleMapper.updateAuthorIdTo_1(userId);
        commentMapper.updateAuthorIdTo_1(userId);
        //最后删除用户
        userMapper.deleteUserById(userId);
        return ResponseMessage.success("成功");
    }

    @Override
    public ResponseMessage<String> forgetPassword(User user) {
        int username_email = userMapper.verifyUsernameAndEmail(user);
        if(username_email == 1){
            //检查邮箱和验证码是否匹配，验证码是否过期
            VerifyCode verifyCode = verifyCodeMapper.get(user.getEmail(),user.getVerifyCode());
            if(verifyCode==null || !verifyCode.isValid()){
                return ResponseMessage.fail(500,"验证码无效");
            }
            Duration duration = Duration.between(verifyCode.getProduceTime(), LocalDateTime.now());
            long minutes = duration.toMinutes();
            if(minutes>10){
                return ResponseMessage.fail(500,"验证码超时，请重新发送");
            }
            user.setPasswordHash(md5.getMD5Hash(user.getPasswordHash()));
            userMapper.updatePassword(user);
            //成功之前，需要将之前的该邮箱之前的验证码全设成无效
            verifyCodeMapper.setInvalidByEmail(user.getEmail());
            return ResponseMessage.success("成功");
        }
        return ResponseMessage.fail(500,"用户名或对应邮箱有误");
    }

    @Override
    public ResponseMessage<String> uploadProfile(MultipartFile file,String token) {
        //MultipartFile file = fileAndToken.getFile();

        if (file.isEmpty()) {
            return ResponseMessage.fail(500,"wrong");
        }

        //获取对应用户
        Token userToken = tokenMapper.getTokenByTokenStr(token);
        if(userToken==null){
            return ResponseMessage.fail(500,"登录过期，请重新登录");
        }
        try {
//            // 获取文件名
//            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
//
//            // 如果文件名包含路径，则只获取最后一个文件名
//            if (fileName.contains(File.separator)) {
//                fileName = fileName.substring(fileName.lastIndexOf(File.separator) + 1);
//            }
//
//            // 防止文件名冲突，使用UUID重命名文件
//            String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;

//            String uniqueFileName = StringUtils.cleanPath(file.getOriginalFilename());
            String uniqueFileName = userToken.getUserId() + ".png";

            // 创建上传目录（如果不存在）
            Path uploadPath = Paths.get(UPLOADED_FOLDER).toAbsolutePath().normalize();
            Files.createDirectories(uploadPath);

            // 保存文件到服务器
            Path filePath = uploadPath.resolve(uniqueFileName);
            Files.copy(file.getInputStream(), filePath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
//            //把头像链接上传至数据库
//            User tempUser = new User();
//            tempUser.setId(userToken.getUserId());
//            tempUser.setProfileLink(UPLOADED_FOLDER+"/"+uniqueFileName);
//            userMapper.updateProfile(tempUser);

            return ResponseMessage.success("成功");
            //return new ResponseEntity<>("File uploaded successfully: " + uniqueFileName, HttpStatus.OK);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseMessage.fail(500,"wrong");
            //return new ResponseEntity<>("Failed to upload file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public PersonalCenterInfo getCenterInfoByTokenStr(String tokenStr) {
        PersonalCenterInfo centerInfo = userMapper.getCenterInfoByTokenStr(tokenStr);
        centerInfo.setTotalHits(hitsMapper.totalLaunchedArticleHitsByAuthorId(centerInfo.getId()));

        centerInfo.setReputation(reputationService.calByUserId(centerInfo.getId()));

        // 指定要检查的文件路径
        String filePath = UPLOADED_FOLDER+"/"+centerInfo.getId() +".png";
        // 创建File对象
        File file = new File(filePath);
        // 判断文件是否存在
        centerInfo.setProfileExist(file.exists());

        // 解析字符串为LocalDateTime对象
        LocalDateTime dateTime = centerInfo.getLoginTime();
        // 定义输出格式
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss");
        // 格式化LocalDateTime对象为字符串
        String formattedDateTime = dateTime.format(outputFormatter);
        centerInfo.setLoginTimeChinese(formattedDateTime);
        return centerInfo;
    }

    @Override
    public PersonalCenterInfo getCenterInfoByUserId(int userId) {
        PersonalCenterInfo centerInfo = userMapper.getCenterInfoByUserId(userId);
        centerInfo.setTotalHits(hitsMapper.totalLaunchedArticleHitsByAuthorId(userId));
        centerInfo.setReputation(reputationService.calByUserId(userId));

        // 指定要检查的文件路径
        String filePath = UPLOADED_FOLDER+"/"+centerInfo.getId() +".png";
        // 创建File对象
        File file = new File(filePath);
        // 判断文件是否存在
        centerInfo.setProfileExist(file.exists());

        return centerInfo;
    }

    @Override
    public ResponseMessage<String> changePassword(User user) {
        // 查询用户名是否存在以及获取用户
        User existingUser = userMapper.getUserByUsername(user.getUsername());

        // 校验用户名是否存在
        if (existingUser==null) {
            return ResponseMessage.fail(500, "用户名有误");
        }

        String existingPasswordHash = existingUser.getPasswordHash();
        // 验证用户输入的旧密码是否正确
        if (existingPasswordHash.equals(md5.getMD5Hash(user.getOldPasswordHash()))) {
            // 更新密码
            user.setPasswordHash(md5.getMD5Hash(user.getPasswordHash()));
            userMapper.updatePassword(user);
            return ResponseMessage.success("密码修改成功");
        }
        System.out.println("false ");
        return ResponseMessage.fail(401, "旧密码不正确");

    }

    @Override
    public ResponseMessage<User> getUserByUsername(String username) {
        try {
            if (username == null || username.trim().isEmpty()) {
                return ResponseMessage.fail(400, "用户名不能为空");
            }

            User user = userMapper.getUserByUsername(username);  // 可能抛出 IllegalArgumentException
            return ResponseMessage.success(user);
        } catch (IllegalArgumentException e) {
            return ResponseMessage.fail(404, "用户不存在：" + e.getMessage());
        } catch (Exception e) {
            return ResponseMessage.fail(500, "服务器内部错误：" + e.getMessage());
        }
    }

    @Override
    public ResponseMessage<String> changeInfo(User user) {
        // 查询用户名是否存在以及获取用户
        User existingUser = userMapper.getUserByUsername(user.getUsername());

        // 校验用户名是否存在
        if (existingUser==null) {
            return ResponseMessage.fail(500, "用户名有误");
        }

        existingUser.setEmail(user.getEmail());
        existingUser.setName(user.getName());

        userMapper.updateInfo(user);
        return ResponseMessage.success("个人信息修改成功");

    }

    @Override
    public ResponseMessage<String> setReputationRank() {
        List<PersonalCenterInfo> userList = userMapper.getAllUserId();
        for(PersonalCenterInfo user:userList) {
            user.setReputation(reputationService.calByUserId(user.getId()));
            userMapper.updateReputationByUserId(user);
        }
        return ResponseMessage.success("success");
    }

    @Override
    public List<PersonalCenterInfo> getReputationRank() {
        List<PersonalCenterInfo> userList = userMapper.getReputationRank();
        for(int i=0;i<userList.size();i++){
            PersonalCenterInfo tempUser = userList.get(i);
            tempUser.setRank(i+1);
            userList.set(i,tempUser);
        }
        return userList;
    }

    @Override
    public ResponseMessage<String> sendEmail(String emailAddress) throws MessagingException, GeneralSecurityException, UnsupportedEncodingException {
        Mail mail = new Mail(emailAddress);
        VerifyCode verifyCode = verifyCodeMapper.getByEmail(emailAddress);
        if(verifyCode!=null){
            Duration duration = Duration.between(verifyCode.getProduceTime(), LocalDateTime.now());
            long second = duration.toSeconds();
            if(second<30){  //操作两次间隔需要大于30秒
                return ResponseMessage.fail(500,"请求太频繁，请稍后重试");
            }
        }
        //增加之前，,需要删除该邮箱对应的所有验证码
        verifyCodeMapper.deleteByEmail(emailAddress);
        verifyCodeMapper.add(new VerifyCode(emailAddress,mail.getVerifiCode()));
        return ResponseMessage.success("success");
    }

}
