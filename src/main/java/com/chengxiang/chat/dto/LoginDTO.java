package com.chengxiang.chat.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * @author 程祥
 * @date 2022/11/17 17:22
 */
@Data
public class LoginDTO {
    private String username;

    private String password;
}
