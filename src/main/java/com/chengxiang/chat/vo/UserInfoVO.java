package com.chengxiang.chat.vo;

import lombok.Data;

/**
 * @author 程祥
 * @date 2022/11/28 10:10
 */
@Data
public class UserInfoVO {
    private Integer id;

    private String username;

    private String nickName;

    private String avatar;

    private Integer sex; // 0：男    1：女

    private String phone;

    private String ipAdd;
}
