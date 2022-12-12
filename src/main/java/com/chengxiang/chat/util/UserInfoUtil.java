package com.chengxiang.chat.util;

import com.chengxiang.chat.pojo.User;

/**
 * @author 程祥
 * @date 2022/11/26 0:07
 * 用来存取用户登录信息
 */
public class UserInfoUtil {
    private static final ThreadLocal<String> userThreadLocal = new ThreadLocal();

    /**
     * 保存用户登录信息
     * @param username
     */
    public static void userLogin(String username) {
        userThreadLocal.set(username);
    }

    /**
     * 获取用户
     */
    public static String getUser() {
        return userThreadLocal.get();
    }

    /**
     * 用户登出
     */
    public static void userLogout() {
        userThreadLocal.remove();
    }
}
