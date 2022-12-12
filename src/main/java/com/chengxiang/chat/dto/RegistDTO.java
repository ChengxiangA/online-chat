package com.chengxiang.chat.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * @author 程祥
 * @date 2022/11/18 9:24
 */
@Data
public class RegistDTO {
    @Email(message = "用户名不合法")
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String captcha;
}
