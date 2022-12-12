package com.chengxiang.chat.interceptor;

import com.chengxiang.chat.pojo.ResultBody;
import com.chengxiang.chat.util.TokenUtil;
import com.chengxiang.chat.util.UserInfoUtil;
import com.chengxiang.chat.util.WhiteListUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @author 程祥
 * @date 2022/11/17 17:37
 */
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 是否放行路径
        if(WhiteListUtil.isAllCanGoPath(request.getServletPath())) {
            return true;
        }
        String authorization = request.getHeader("Authorization");
        // 如果没有携带Token请求头
        if(StringUtils.isEmpty(authorization) || !TokenUtil.verifyToken(authorization)) {
            response.setContentType("context/html;charset=utf-8");
            response.setContentType("application/json");
            ResultBody tokenFalse = ResultBody.error("请先登录!");
            PrintWriter writer = response.getWriter();
            String info = new ObjectMapper().writeValueAsString(tokenFalse);
            writer.print(info);
            writer.flush();
            writer.close();
            return false;
        }
        // Token验证通过
        String username = TokenUtil.getUsernameFromToken(authorization);
        UserInfoUtil.userLogin(username);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
