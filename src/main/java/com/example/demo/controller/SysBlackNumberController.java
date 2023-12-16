package com.example.demo.controller;

import cn.hutool.core.bean.BeanUtil;
import com.example.demo.dto.req.SysBlackNumberInsertReq;
import com.example.demo.dto.req.SysBlackNumberReq;
import com.example.demo.dto.resp.*;
import com.example.demo.entity.SysBlackNumberEntity;
import com.example.demo.service.SysBlackNumberService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/blackNumber")
public class SysBlackNumberController {
    @Resource
    private SysBlackNumberService service;

    @GetMapping("/getAll")
    public Response<List<SysBlackNumberResp>> listResponse (){
        List<SysBlackNumberEntity> sysBlackNumberEntities = service.getAllBlackNumber();
        if (Objects.isNull(sysBlackNumberEntities)){
            return ResponseUtil.create(ResponseCodeEnum.UPDATE_FAIL,null);
        }
        List<SysBlackNumberResp> resps = sysBlackNumberEntities.stream()
                .map(entity -> BeanUtil.copyProperties(entity,SysBlackNumberResp.class)).collect(Collectors.toList());
        return ResponseUtil.create(ResponseCodeEnum.OK,resps);
    }

    @PostMapping("/delete")
    public Response<SysBlackNumberDeleteResp> listResponse(@RequestBody SysBlackNumberReq req){
        SysBlackNumberDeleteResp sysBlackNumberEntities = service.deleteBlackNumber(req);
        if (Objects.isNull(sysBlackNumberEntities)){
            return ResponseUtil.create(ResponseCodeEnum.UPDATE_FAIL,null);
        }
        return ResponseUtil.create(ResponseCodeEnum.OK,sysBlackNumberEntities);
    }

    @PostMapping("/insert")
    public Response<SysBlackNumberDeleteResp> sysBlackNumberDeleteRespResponse (@RequestBody SysBlackNumberInsertReq req){
        SysBlackNumberDeleteResp sysBlackNumberDeleteResp = service.insertNumber(req);
        if (Objects.isNull(sysBlackNumberDeleteResp)){
            return ResponseUtil.create(ResponseCodeEnum.UPDATE_FAIL,null);
        }
        return ResponseUtil.create(ResponseCodeEnum.OK,sysBlackNumberDeleteResp);
    }
}
