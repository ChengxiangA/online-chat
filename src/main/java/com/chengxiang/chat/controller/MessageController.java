package com.chengxiang.chat.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chengxiang.chat.constant.SocketConstant;
import com.chengxiang.chat.dao.MessageMapper;
import com.chengxiang.chat.pojo.Message;
import com.chengxiang.chat.pojo.ResultBody;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * @author 程祥
 * @date 2022/11/17 14:26
 */
@RestController
@Api(tags = "消息接口")
public class MessageController {
    @Autowired
    private MessageMapper messageMapper;

    /**
     * 获取近期20条消息
     * 所有人
     * @return
     */
    @GetMapping("/message/get")
    public ResultBody getMessage(@RequestParam("pageNo") Integer pageNo) {
        Page<Message> messagePage = new Page<>(pageNo, SocketConstant.pageSize, false);
        messageMapper.selectPage(messagePage, new LambdaQueryWrapper<Message>()
                .orderByDesc(Message::getCreateTime));
        List<Message> records = messagePage.getRecords();
        return ResultBody.success("成功获取近期20条聊天记录",records);
    }

    /**
     * 获取三天以内的所有消息
     */
    @GetMapping("message/threeday")
    public ResultBody getMessageInThreeDay() {
        List<Message> messages = messageMapper.selectList(new LambdaQueryWrapper<Message>()
                        .orderByAsc(Message::getCreateTime).gt(Message::getCreateTime, new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24 * 3)));
        return ResultBody.success("成功获取三天内所有消息",messages);
    }
}
