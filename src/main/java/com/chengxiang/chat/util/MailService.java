package com.chengxiang.chat.util;

import com.chengxiang.chat.constant.SocketConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * @author 程祥
 * @date 2022/11/19 19:55
 */
@Service
public class MailService {

    @Value("${spring.mail.username}")
    private String from;
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendMail(String to,String subject,String content) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(this.from);
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(SocketConstant.registEmail.replace("{}",content));
        javaMailSender.send(simpleMailMessage);
    }
}
