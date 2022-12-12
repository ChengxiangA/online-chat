package com.chengxiang.chat.config;

import com.chengxiang.chat.constant.SocketConstant;
import com.chengxiang.chat.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 程祥
 * @date 2022/11/17 17:32
 */
@Configuration
public class MyWebMvcConfig implements WebMvcConfigurer {

    @Value("${upload.local.path}")
    private String path;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/**")
                .excludePathPatterns("/login","/regist","/captcha","/upload/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**") //虚拟url路径
//                .addResourceLocations("file:" + path + SocketConstant.AVATAR_PATH);
                .addResourceLocations("file:E:/images/"); // 用来测试
    }
}
