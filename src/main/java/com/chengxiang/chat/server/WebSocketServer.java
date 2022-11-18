package com.chengxiang.chat.server;

import com.chengxiang.chat.pojo.Message;
import com.chengxiang.chat.util.MessageDecoder;
import com.chengxiang.chat.util.MessageEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.time.LocalDateTime;
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
    private static int onlineCount = 0;

    //
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
        log.info("用户{}进入聊天室", username);
        websocketMap.put(username, this);
        try {
            Message message = SomeOneComeIn(username);
            sendMessage(message);
        } catch (IOException | EncodeException e) {
            log.error("WEBSOCKET IO异常");
        }
    }

    @OnMessage
    public void onMessage(Message message) {
        log.info("收到来自窗口"+ this.username + "的信息:" + message);
        try {
            message.setFrom(this.username);
            sendMessage(message);
        } catch (IOException | EncodeException e) {
            log.error("WEBSOCKET IO异常");
        }
    }

    @OnClose
    public void onClose() throws EncodeException, IOException {
        log.info("用户" + this.username + "离开了");
        websocketMap.remove(this.username);
        Message message = SomeOneOut(this.username);
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
            // 测试
            ws.getSession().getBasicRemote().sendObject(message);
//            if(this != ws) {
//                ws.send2One(ws.getSession(),message);
//            }
        }
    }

    public void sendMessage(Message message) throws EncodeException, IOException {
        if(message.getTo().equals("*")) {
            send2All(message);
        } else {
            send2One(message);
        }
    }

    public Message SomeOneComeIn(String username) {
        Message message = new Message();
        message.setMessage("用户" + username + "进入聊天室");
        message.setFrom("888");
        message.setTo("*");
        message.setDate(new Date());
        return message;
    }

    public Message SomeOneOut(String username) {
        Message message = new Message();
        message.setMessage("用户" + username + "离开聊天室");
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

}
