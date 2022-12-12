package com.chengxiang.chat.util;


import com.chengxiang.chat.exception.BizException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 程祥
 * @date 2022/11/17 18:03
 */
public class TokenUtil {
    private static final long EXPIRE_TIME = 60 * 60 * 1000; // 1小时

    private static  final String TOKEN_SECRET = "chengxiangchengxiangchengxiangchengxiangchengxiangchengxiang";

    // 获取测试token
//eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6ImFkbWluQHFxLmNvbSIsImlhdCI6MTY2OTc4MzM2OSwiZXhwIjoxNjczMzgzMzY5fQ.bHMa-Rnq36a-SYfCo8oSVWu-DZv7_9KhXlkrkDYYB4s
//    public static void main(String[] args) {
//        String token = getToken("admin@qq.com");
//        System.out.println(token);
//    }

    public static String getToken(String username) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        //创建时间
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date expire = new Date(nowMillis + EXPIRE_TIME);
        Map<String,Object> claims = new HashMap<>();
        claims.put("username",username);
        String token = "";
        try {
            token = Jwts.builder()
                    .setClaims(claims)
                    .setIssuedAt(now)
                    .setExpiration(expire)
                    .signWith(signatureAlgorithm,TOKEN_SECRET).compact();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return token;
    }

    // 验证Token
    public static boolean verifyToken(String token) {
        Claims claimsFromToken = getClaimsFromToken(token);
        if(claimsFromToken != null) {
            Date expiration = claimsFromToken.getExpiration();
            if(new Date().after(expiration)) {
                return false;
            }
        }
        return true;
    }

    public static String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        String username = "";
        if(claims != null) {
            username = claims.get("username", String.class);
        }
        return username;
    }

    public static Claims getClaimsFromToken(String token) {
        Claims claims = null;
        try {
             claims = Jwts.parserBuilder().setSigningKey(TOKEN_SECRET).build().parseClaimsJws(token).getBody();
        } catch (Exception e) {
            throw new BizException("无效Token，请重新获取！！");
        }
        return claims;
    }

    public static String getUsernameFromHeader(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        return getUsernameFromToken(authorization);
    }


}
