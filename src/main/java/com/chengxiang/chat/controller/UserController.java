package com.chengxiang.chat.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chengxiang.chat.dao.UserMapper;
import com.chengxiang.chat.dto.LoginDTO;
import com.chengxiang.chat.dto.RegistDTO;
import com.chengxiang.chat.pojo.ResultBody;
import com.chengxiang.chat.pojo.User;
import com.chengxiang.chat.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author 程祥
 * @date 2022/11/17 14:30
 */
@RestController
public class UserController {
    @Autowired
    UserMapper userMapper;

    @PostMapping("/regist")
    public ResultBody regist(@RequestBody @Valid RegistDTO registDTO) {
        User dbUser = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, registDTO.getUsername()));
        if(dbUser != null) {
            return ResultBody.error("该用户名已存在");
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
