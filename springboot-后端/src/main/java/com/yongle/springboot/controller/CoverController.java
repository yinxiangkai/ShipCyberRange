package com.yongle.springboot.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yongle.springboot.common.Result;

import com.yongle.springboot.service.ICoverService;
import com.yongle.springboot.mapper.CoverMapper;
import com.yongle.springboot.entity.Cover;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yongle
 * @since 2023-03-05
 */
@RestController
@RequestMapping("/cover")
public class CoverController {

    @Resource
    private ICoverService coverService;
    @Resource
    private CoverMapper coverMapper;

    // 新增或者更新
    @PostMapping("/save")
    public Result save(@RequestBody Cover cover) {

        if ( cover.getId() == null) {
         coverMapper.insert(cover);
        return Result.success(null,"新增成功");
        }else{
         coverMapper.updateById(cover);
        return Result.success(null,"更新成功");
        }
        }

    @DeleteMapping("/del/{id}")
    public Result delete(@PathVariable Integer id) {
        coverService.removeById(id);
        return Result.success(null,"删除成功");
    }


    @GetMapping("/page")
    public Result findPage( @RequestParam(defaultValue = "") String name,
                            @RequestParam Integer pageNum,
                            @RequestParam Integer pageSize) {
        QueryWrapper<Cover> queryWrapper = new QueryWrapper<>();
        if (!"".equals(name)) {
        queryWrapper.like("name", name);
        }
        return Result.success(coverService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }
    @GetMapping("/get")
    public Result get() {
        return Result.success(coverMapper.selectList(null));
    }
}

