package com.chengxiang.chat.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author 程祥
 * @date 2022/11/17 13:24
 */
@Data
@TableName("MESSAGE")
public class Message {
    @TableField(value = "from_who")
    private String from;

    @TableField(value = "to_who")
    private String to;

    private String message;

    @JsonProperty("time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time")
    private Date date;
}
