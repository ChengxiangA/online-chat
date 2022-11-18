package com.chengxiang.chat.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author 程祥
 * @date 2022/11/17 14:26
 */
@Data
@TableName("USERS")
public class User {
    private Integer id;

    private String username;

    private String password;

    private String name;

    private Integer age;

    private String sex;

    private String phone;

    private Date createTime;
}
