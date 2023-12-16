package com.example.demo.service.impl;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.dto.req.SysLoginInsertReq;
import com.example.demo.dto.req.SysLoginReq;
import com.example.demo.dto.resp.SysLoginResp;
import com.example.demo.entity.SysLoginEntity;
import com.example.demo.mapper.SysLoginMapper;
import com.example.demo.service.SysLoginService;
import com.example.demo.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class SysLoginServiceImpl extends ServiceImpl<SysLoginMapper, SysLoginEntity> implements SysLoginService {

    @Resource
    private SysLoginMapper mapper;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public String login(SysLoginReq req) {
        LambdaQueryWrapper<SysLoginEntity> sysLoginEntityLambdaQueryWrapper = new QueryWrapper<SysLoginEntity>().lambda()
                .eq(SysLoginEntity::getId,req.getUsername())
                .eq(SysLoginEntity::getPassword,req.getPassword());
        SysLoginEntity sysLoginEntity = mapper.selectOne(sysLoginEntityLambdaQueryWrapper);
        if (Objects.isNull(sysLoginEntity)){
            return null;
        }
        try {
            Map<String,Object> claims = new HashMap<>();
            claims.put("userId",req.getUsername());
            String token = JwtUtil.sign(claims);
            redisTemplate.opsForValue().set("login:"+ token,req.getUsername(),600, TimeUnit.SECONDS);
            return token;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<SysLoginResp> getAll() {
        return mapper.getAll();
    }

    @Override
    public String regist(SysLoginInsertReq req) {
        Date currentDate = new Date();
        LambdaQueryWrapper<SysLoginEntity> lambdaQueryWrapper = new QueryWrapper<SysLoginEntity>().lambda()
                .eq(SysLoginEntity::getId,req.getId());
        SysLoginEntity sysLoginEntity = mapper.selectOne(lambdaQueryWrapper);
        if (Objects.isNull(sysLoginEntity)){
            int status = 0;
            SysLoginEntity sysLoginEntity1 = new SysLoginEntity(req.getId(),req.getUserName(),req.getPhoneNumber(),req.getRoler(),req.getBeiZhu(),currentDate,req.getPassword(),status);
            mapper.insert(sysLoginEntity1);
            return "success";
        }
        return null;
    }

    @Override
    public SysLoginEntity getByUsername(String username) {
        LambdaQueryWrapper<SysLoginEntity> sysLoginEntityLambdaQueryWrapper = new QueryWrapper<SysLoginEntity>().lambda()
                .eq(SysLoginEntity::getUserName,username);
        return mapper.selectOne(sysLoginEntityLambdaQueryWrapper);
    }

    @Override
    public List<String> getUserPermissions(String username) {
        LambdaQueryWrapper<SysLoginEntity> sysLoginEntityLambdaQueryWrapper = new QueryWrapper<SysLoginEntity>().lambda()
                .eq(SysLoginEntity::getUserName,username);
        SysLoginEntity sysLoginEntity = mapper.selectOne(sysLoginEntityLambdaQueryWrapper);
        if (Objects.isNull(sysLoginEntity)){
            throw new RuntimeException("没有这个用户");
        }
        // 获取用户的角色信息
        String userRole = sysLoginEntity.getRoler();

        // 将角色信息映射成Spring Security的权限格式
        List<String> permissions = new ArrayList<>();
        if ("admin".equals(userRole)) {
            permissions.add("ROLE_ADMIN");
        } else if ("user".equals(userRole)) {
            permissions.add("ROLE_USER");
        }
        return permissions;
    }
}
