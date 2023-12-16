package com.example.demo.Interceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.Exception.TeamException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
public class JwtTokenInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取请求头中的 Authorization 字段
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            String jsonString = redisTemplate.opsForValue().get("login:" + token);
            if (!Objects.isNull(jsonString)){
                DecodedJWT decode = JWT.decode(token);
                Claim header = decode.getClaim("userId");
                String userIdFromJWT = header.asString();
                if (!userIdFromJWT.equals(jsonString)) {
                    throw TeamException.Login_ShiXiao;
                }
                redisTemplate.opsForValue().set("login:" + token, jsonString, 600, TimeUnit.SECONDS);
            }else {
                throw new RuntimeException("用户未登录");
            }
        }else {
            return true;
        }
        return true;
    }


}

