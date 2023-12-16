package com.example.demo.controller;

import cn.hutool.core.bean.BeanUtil;
import com.example.demo.dto.req.SysFxsDeleteReq;
import com.example.demo.dto.resp.*;
import com.example.demo.entity.SysFxsEntity;
import com.example.demo.service.SysFxsService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/Fxs")
public class SysFxsController {
    @Resource
    private SysFxsService service;

    @GetMapping("/getAll")
    public Response<List<SysFxsResp>> response (){
        List<SysFxsEntity>sysFxsEntity = service.getAll();
        if (Objects.isNull(sysFxsEntity)){
            return ResponseUtil.create(ResponseCodeEnum.UPDATE_FAIL,null);
        }
        List<SysFxsResp> resps = sysFxsEntity.stream()
                .map(entity ->
                BeanUtil.copyProperties(entity, SysFxsResp.class))
                .collect(Collectors.toList());
        return ResponseUtil.create(ResponseCodeEnum.OK,resps);
    }

    @PostMapping("/insert")
    public Response<String> stringResponse (@RequestBody SysFxsDeleteReq req){
        String a = service.insert(req);
        if (Objects.isNull(a)){
            return ResponseUtil.create(ResponseCodeEnum.UPDATE_FAIL,null);
        }
        return ResponseUtil.create(ResponseCodeEnum.OK,null);
    }

    @PostMapping("/delete")
    public Response<String> stringResponse1 (@RequestBody SysFxsDeleteReq req){
        String a = service.delete(req);
        if (Objects.isNull(a)){
            return ResponseUtil.create(ResponseCodeEnum.UPDATE_FAIL,null);
        }
        return ResponseUtil.create(ResponseCodeEnum.OK,null);
    }
}
