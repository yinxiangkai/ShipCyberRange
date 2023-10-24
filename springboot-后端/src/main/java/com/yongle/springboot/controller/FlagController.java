package com.yongle.springboot.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yongle.springboot.common.Result;

import com.yongle.springboot.service.IFlagService;
import com.yongle.springboot.mapper.FlagMapper;
import com.yongle.springboot.entity.Flag;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yongle
 * @since 2023-03-07
 */
@RestController
@RequestMapping("/flag")
public class FlagController {

    @Resource
    private IFlagService flagService;
    @Resource
    private FlagMapper flagMapper;

    // 新增或者更新
    @PostMapping("/save")
    public Result save(@RequestBody Flag flag) {
        return flagService.insertFlag(flag);
    }

    @DeleteMapping("/del/{id}")
    public Result delete(@PathVariable Integer id) {
        flagService.removeById(id);
        return Result.success(null,"删除成功");
    }

    @GetMapping("/page")
    public Result findPage( @RequestParam(defaultValue = "") String rangceName,
                            @RequestParam Integer userId,
                            @RequestParam Integer pageNum,
                            @RequestParam Integer pageSize) {
        return Result.success(flagService.findPage(new Page<>(pageNum, pageSize), rangceName,userId));
    }

}

