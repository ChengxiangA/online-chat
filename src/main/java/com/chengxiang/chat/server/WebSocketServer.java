package com.chengxiang.chat.server;

import com.chengxiang.chat.dao.MessageMapper;
import com.chengxiang.chat.pojo.Message;
import com.chengxiang.chat.util.MessageDecoder;
import com.chengxiang.chat.util.MessageEncoder;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 程祥
 * @date 2022/11/17 11:10
 */
@ServerEndpoint(value = "/ws/{username}",decoders = MessageDecoder.class,encoders = MessageEncoder.class)
@Component
@Slf4j
public class WebSocketServer {

    private static MessageMapper messageMapper;

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public void messageMapper(MessageMapper messageMapper) {
        this.messageMapper = messageMapper;
    }
    // 在线人数
    private static int onlineCount = 0;

    // 用于主动推送消息会话
    private Session session;

    // 记录所有在线用户
    private static ConcurrentHashMap<String,WebSocketServer> websocketMap = new ConcurrentHashMap<>();

    private String username;

    public Session getSession() {
        return session;
    }

    public String getUsername() {
        return username;
    }


    @OnOpen
    public void onOpen(Session session,@PathParam("username") String username) {
        this.session = session;
        this.username = username;
        addOnlineCount();
        log.info("用户{}进入聊天室,当前在线人数{}人", username,getOnlineCount());
        websocketMap.put(username, this);
        try {
            Message message = SomeOneComeIn();
            messageMapper.insert(message);
            sendMessage(message);
        } catch (IOException | EncodeException e) {
            log.error("WEBSOCKET IO异常");
        }
    }

    @OnMessage
    public void onMessage(Message message) {
        log.info("收到来自窗口"+ this.username + "的信息:" + message);
        log.info(this + "");
        try {
            message.setFrom(this.username);
            message.setDate(new Date());
            messageMapper.insert(message);
            sendMessage(message);
        } catch (IOException | EncodeException e) {
            log.error("WEBSOCKET IO异常");
        }
    }

    @OnClose
    public void onClose() throws EncodeException, IOException {
        subOnlineCount();
        log.info("用户{}离开聊天室,当前在线人数{}人", username,getOnlineCount());
        websocketMap.remove(this.username);
        Message message = SomeOneOut();
        messageMapper.insert(message);
        sendMessage(message);
    }

    @OnError
    public void onError(Throwable e) throws EncodeException, IOException {
        log.info("服务器异常，请检查异常情况！");
        sendMessage(error());
    }

    // 根据用户找到指定发送人
    public void send2One(Message message) throws IOException, EncodeException {
        WebSocketServer webSocketServer = websocketMap.get(message.getTo());
        Session session = webSocketServer.getSession();
        session.getBasicRemote().sendObject(message);
        // 确保自己的消息发送成功
        this.session.getBasicRemote().sendObject(message);
    }

    public void send2All(Message message) throws IOException, EncodeException {
        for (WebSocketServer ws : websocketMap.values()) {
            ws.getSession().getBasicRemote().sendObject(message);
        }
    }

    public void sendMessage(Message message) throws EncodeException, IOException {
        if(message.getTo().equals("*")) {
            send2All(message);
        } else {
            send2One(message);
        }
    }

    public Message SomeOneComeIn() {
        Message message = new Message();
        message.setMessage("用户" + this.username + "进入聊天室,当前在线人数" + getOnlineCount() + "人");
        message.setFrom("888");
        message.setTo("*");
        message.setDate(new Date());
        return message;
    }

    public Message SomeOneOut() {
        Message message = new Message();
        message.setMessage("用户" + this.username + "离开聊天室,当前在线人数" + getOnlineCount() + "人");
        message.setFrom("888");
        message.setTo("*");
        message.setDate(new Date());
        return message;
    }

    public Message error() {
        Message message = new Message();
        message.setMessage("服务器异常，请稍后再试！");
        message.setFrom("888");
        message.setTo("*");
        message.setDate(new Date());
        return message;
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized int addOnlineCount() {
        return onlineCount ++;
    }

    public static synchronized int subOnlineCount() {
        return onlineCount --;
    }
}
