package com.chengxiang.chat.util;

import com.chengxiang.chat.pojo.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

/**
 * @author 程祥
 * @date 2022/11/17 17:14
 */
public class MessageDecoder implements Decoder.Text<Message> {

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Message decode(String s) throws DecodeException {
        try {
            return objectMapper.readValue(s,Message.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean willDecode(String s) {
        return s != null;
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
