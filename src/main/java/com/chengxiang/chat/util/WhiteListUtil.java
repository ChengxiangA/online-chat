package com.chengxiang.chat.util;

import com.chengxiang.chat.constant.SocketConstant;
import org.springframework.util.AntPathMatcher;

/**
 * @author 程祥
 * @date 2022/11/25 17:18
 */
public class WhiteListUtil {
    private static final AntPathMatcher pathMatcher = new AntPathMatcher();

    // 是否为不需要授权登录路径
    public static boolean isAllCanGoPath(String path) {
        for(int i = 0;i < SocketConstant.addWhite.length;i ++) {
            if(pathMatcher.match(SocketConstant.addWhite[i],path)) {
                return true;
            }
        }
        return false;
    }

}
