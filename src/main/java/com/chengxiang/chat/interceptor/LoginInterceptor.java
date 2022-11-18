package com.chengxiang.chat.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chengxiang.chat.pojo.ResultBody;
import com.chengxiang.chat.util.TokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
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
