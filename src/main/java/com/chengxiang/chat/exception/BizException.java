package com.chengxiang.chat.exception;

/**
 * @author 程祥
 * @date 2022/11/17 15:26
 */
public class BizException extends RuntimeException {
    public BizException() {
    }

    public BizException(String message) {
        super(message);
    }
}
