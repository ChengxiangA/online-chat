package com.chengxiang.chat.exception;


import com.chengxiang.chat.pojo.ResultBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

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

    /**
     * ResponseBody @valid 不合法
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultBody MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining());
        return ResultBody.error(message);
    }

    @ExceptionHandler(BizException.class)
    public ResultBody bizException(BizException e) {
        log.error(e.getMessage());
        return ResultBody.error(e.getMessage());
    }



}
