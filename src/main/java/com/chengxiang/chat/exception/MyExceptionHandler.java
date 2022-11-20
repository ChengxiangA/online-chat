package com.chengxiang.chat.exception;


import com.chengxiang.chat.pojo.ResultBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author 程祥
 * @date 2022/11/17 15:29
 */
@RestControllerAdvice
@Slf4j
public class MyExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResultBody handler(Exception e) {
        log.error(e.getMessage());
        return ResultBody.error("出错了，请联系管理员");
    }
}
