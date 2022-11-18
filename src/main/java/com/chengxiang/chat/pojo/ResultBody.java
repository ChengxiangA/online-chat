package com.chengxiang.chat.pojo;

import lombok.Data;
import lombok.Getter;

/**
 * @author 程祥
 * @date 2022/11/17 15:21
 */

@Data
public class ResultBody<T> {
    private Integer status;

    private String message;

    private T data;

    public ResultBody(Integer status) {
        this.status = status;
    }

    public ResultBody(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public ResultBody(Integer status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static ResultBody error(String message) {
        return new ResultBody(222,message);
    }

    public static ResultBody success(String message) {
        return new ResultBody(200,message);
    }

}
