package com.chengxiang.chat.constant;

/**
 * @author 程祥
 * @date 2022/11/17 11:06
 */
public class SocketConstant {
    public static final String[] IpWhiteList = {"*"};

    public static final String registEmail = "您正在注册聊天室账号，您的验证码为：{}，有效期10分钟，请勿将此验证码透露给任何人哟！";

    public static final String[] addWhite = {"/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**","doc.html"};

    // 分页大小
    public static final Integer pageSize = 20;

    // Redis token key前缀
    public static final String TOKEN_KEY = "Token:";

    // 图片上传路径
    public static final String AVATAR_PATH = "/avatar/";
}
