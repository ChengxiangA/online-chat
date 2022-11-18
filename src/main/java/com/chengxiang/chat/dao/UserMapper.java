package com.chengxiang.chat.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chengxiang.chat.pojo.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 程祥
 * @date 2022/11/17 15:10
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
