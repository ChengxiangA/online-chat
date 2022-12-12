package com.chengxiang.chat.vo;

import lombok.Data;

/**
 * @author 程祥
 * @date 2022/11/28 9:57
 */
@Data
public class LoginVO {
    private String token;
    // 用户昵称
    private String nickName;
}
