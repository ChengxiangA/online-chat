package com.chengxiang.chat.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author 程祥
 * @date 2022/11/18 9:24
 */
@Data
public class RegistDTO {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String name;

    private Integer age;

    private String sex;

    private String phone;
}
