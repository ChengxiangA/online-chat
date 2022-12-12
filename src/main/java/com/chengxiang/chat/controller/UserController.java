package com.chengxiang.chat.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chengxiang.chat.constant.SocketConstant;
import com.chengxiang.chat.dao.UserMapper;
import com.chengxiang.chat.dto.LoginDTO;
import com.chengxiang.chat.dto.RegistDTO;
import com.chengxiang.chat.dto.UserInfoDTO;
import com.chengxiang.chat.pojo.ResultBody;
import com.chengxiang.chat.pojo.User;
import com.chengxiang.chat.util.*;
import com.chengxiang.chat.vo.LoginVO;
import com.chengxiang.chat.vo.UserInfoVO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.concurrent.TimeUnit;

/**
 * @author 程祥
 * @date 2022/11/17 14:30
 */
@RestController
@Api(tags = "用户登陆注册接口")
public class UserController {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MailService mailService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${baidu.ak}")
    private String baiduAk;

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
        // 生成一个随机昵称
        user.setNickName(RandomName.randomName(true,3));
        userMapper.insert(user);
        return ResultBody.success("注册成功");
    }

    @PostMapping("/login")
    public ResultBody login(@RequestBody LoginDTO loginDTO) {
        User dbUser = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, loginDTO.getUsername()));
        if(dbUser == null || !dbUser.getPassword().equals(loginDTO.getPassword())) {
            return ResultBody.error("用户名不存在或密码错误");
        }
        String token = TokenUtil.getToken(loginDTO.getUsername());
        // 只允许一个用户登录
        redisTemplate.opsForValue().set("Token:" + loginDTO.getUsername(),token);
        // 使用ThreadLocal存储登录用户
        UserInfoUtil.userLogin(loginDTO.getUsername());
        LoginVO loginVO = new LoginVO();
        loginVO.setToken(token);
        loginVO.setNickName(dbUser.getNickName());
        return new ResultBody(200,"登陆成功", loginVO);
    }

    @GetMapping("/logout")
    public ResultBody logout() {
        String username = UserInfoUtil.getUser();
        // Redis中删除对应用户
        redisTemplate.delete(SocketConstant.TOKEN_KEY + username);
        // 清楚用户信息 同时需要前端删除本地token
        UserInfoUtil.userLogout();
        return ResultBody.success("成功登出");
    }

    /**
     * 获取登录用户信息
     * @param request
     * @return
     */
    @GetMapping("/info/query")
    public ResultBody info(HttpServletRequest request) {
        // 通过ip查询地址 因为大多数为局域网访问 暂时不用
//        String url = "https://api.map.baidu.com/location/ip?ak=%s&ip=%s&coor=bd09ll";
//        String ipAddress = IPUtil.getIpAddress(request);
//        String json = restTemplate.getForObject(String.format(url, baiduAk, ipAddress), String.class);
//        JSONObject jsonObject = JSONObject.parseObject(json);
//        if(!jsonObject.getObject("status",Integer.class).equals(0)) {
//            return ResultBody.error(jsonObject.getObject("message",String.class));
//        } else {
//            String address = (String) ((Map) jsonObject.get("content")).get("address");
//            return ResultBody.success("成功获取用户登录信息", address);
//        }
        String ipAddress = IPUtil.getIpAddress(request);
        String username = UserInfoUtil.getUser();
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.setUsername(username);
        userInfoVO.setAvatar(user.getAvatar());
        userInfoVO.setId(user.getId());
        userInfoVO.setSex(user.getSex());
        userInfoVO.setIpAdd(ipAddress);
        userInfoVO.setNickName(user.getNickName());
        userInfoVO.setPhone(user.getPhone());
        return ResultBody.success("查询成功",userInfoVO);
    }


    @PostMapping("/info/update")
    public ResultBody update(@RequestBody @Valid UserInfoDTO userInfoDTO) {
//        userMapper.updateById()
        User dbUser = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getId, userInfoDTO.getId()));
        return null; // todo
    }
}
