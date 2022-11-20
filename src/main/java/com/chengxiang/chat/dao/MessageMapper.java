package com.chengxiang.chat.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chengxiang.chat.pojo.Message;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 程祥
 * @date 2022/11/18 14:58
 */
@Mapper
public interface MessageMapper extends BaseMapper<Message> {
}
