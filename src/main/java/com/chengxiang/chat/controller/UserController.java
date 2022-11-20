package com.chengxiang.chat.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chengxiang.chat.dao.UserMapper;
import com.chengxiang.chat.dto.LoginDTO;
import com.chengxiang.chat.dto.RegistDTO;
import com.chengxiang.chat.pojo.ResultBody;
import com.chengxiang.chat.pojo.User;
import com.chengxiang.chat.util.MailService;
import com.chengxiang.chat.util.RandomUtil;
import com.chengxiang.chat.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.concurrent.TimeUnit;

/**
 * @author 程祥
 * @date 2022/11/17 14:30
 */
@RestController
public class UserController {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MailService mailService;

    @Autowired
    private RedisTemplate redisTemplate;

    // 发送注册验证码
    @GetMapping("/captcha")
    public ResultBody captcha(@RequestParam("username") String username) {
        String captcha = RandomUtil.captcha();
        // 一分钟内重复申请验证码
        if(redisTemplate.opsForValue().get(username) != null || redisTemplate.getExpire(username) >= 540) {
            return ResultBody.error("请一分钟后再试!");
        }
        mailService.sendMail(username,"注册验证码", captcha);
        redisTemplate.opsForValue().set(username,captcha,600, TimeUnit.SECONDS); // 有效期十分钟
        return ResultBody.success("验证码已发送!");
    }

    @PostMapping("/regist")
    public ResultBody regist(@RequestBody @Valid RegistDTO registDTO) {
        User dbUser = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, registDTO.getUsername()));
        if(dbUser != null) {
            return ResultBody.error("该用户名已存在");
        } else if(redisTemplate.opsForValue().get(registDTO.getUsername()) == null || !registDTO.getCaptcha().equals(redisTemplate.opsForValue().get(registDTO.getUsername()))) {
            return ResultBody.error("验证码错误!");
        }
        User user = new User();
        user.setUsername(registDTO.getUsername());
        user.setPassword(registDTO.getPassword());
        user.setName(registDTO.getName());
        user.setAge(registDTO.getAge());
        user.setSex(registDTO.getSex());
        user.setPhone(registDTO.getPhone());
        userMapper.insert(user);
        return ResultBody.success("注册成功");
    }

    @PostMapping("/login")
    public ResultBody login(@RequestBody @Valid LoginDTO loginDTO) {
        User dbUser = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, loginDTO.getUsername()));
        if(dbUser == null || !dbUser.getPassword().equals(loginDTO.getPassword())) {
            return ResultBody.error("用户名不存在或密码错误");
        }
        return new ResultBody(200,"登陆成功", TokenUtil.getToken(loginDTO.getUsername()));
    }

    
}
