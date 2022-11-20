package com.chengxiang.chat.util;

import java.util.Random;

/**
 * @author 程祥
 * @date 2022/11/20 12:12
 */
public class RandomUtil {
    private static Random random = new Random();


    // 生成随机6位验证码
    public static String captcha() {
        StringBuilder str = new StringBuilder();
        for(int i = 1;i <= 6;i ++) {
            str.append(random.nextInt(10));
        }
        return str.toString();
    }
}
