package com.yongle.springboot.controller;


import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

import com.yongle.springboot.common.Result;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yongle
 * @since 2023-03-04
 */
@RestController
@RequestMapping("/task")
public class TaskController {


    @Resource
    private StringRedisTemplate stringRedisTemplate;

    // 新增或者更新
    @GetMapping("/status/{id}")
    public Result status(@PathVariable Integer id) {
        String status=stringRedisTemplate.opsForValue().get(id.toString());
        if(status.equals("FINISHED")){
            stringRedisTemplate.delete(String.valueOf(id));
        }
        return Result.success(status);
    }

}

