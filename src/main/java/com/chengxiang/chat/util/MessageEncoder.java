package com.chengxiang.chat.util;

import com.chengxiang.chat.pojo.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 * @author 程祥
 * @date 2022/11/17 17:14
 */
public class MessageEncoder implements Encoder.Text<Message> {

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String encode(Message message) throws EncodeException {
        try {
            return objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
