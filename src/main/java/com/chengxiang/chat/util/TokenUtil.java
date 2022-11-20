package com.chengxiang.chat.util;


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
        return Jwts.parserBuilder().setSigningKey(TOKEN_SECRET).build().parseClaimsJws(token).getBody();
    }

    public static String getUsernameFromHeader(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        return getUsernameFromToken(authorization);
    }


}
