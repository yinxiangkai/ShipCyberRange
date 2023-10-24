package com.yongle.springboot.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yongle.springboot.entity.dto.VisData;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yongle.springboot.common.Result;

import com.yongle.springboot.service.IRangceHostService;
import com.yongle.springboot.mapper.RangceHostMapper;
import com.yongle.springboot.entity.RangceHost;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yongle
 * @since 2023-04-16
 */
@RestController
@RequestMapping("/rangce-host")
public class RangceHostController {

    @Resource
    private IRangceHostService rangceHostService;
    @Resource
    private RangceHostMapper rangceHostMapper;

    // 新增或者更新
    @PostMapping("/save")
    public Result save(@RequestBody RangceHost rangceHost) {

        if ( rangceHost.getId() == null) {
         rangceHostMapper.insert(rangceHost);
        return Result.success(null,"新增成功");
        }else{
         rangceHostMapper.updateById(rangceHost);
        return Result.success(null,"更新成功");
        }
        }

    @DeleteMapping("/del/{id}")
    public Result delete(@PathVariable Integer id) {
        rangceHostService.removeById(id);
        return Result.success(null,"删除成功");
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        rangceHostService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping("/map")
    public Result getMap( @RequestParam(defaultValue = "") String rangceId,
                          @RequestParam Integer userId) {
        VisData visData=rangceHostService.getvisMap(rangceId,userId);
        return Result.success(visData);
    }
    @GetMapping("/getDownList")
    public Result getDownList( @RequestParam String rangceId,
                               @RequestParam Integer userId) {
        return  rangceHostService.getDownList(rangceId,userId);
    }

    @GetMapping("/page")
    public Result findPage( @RequestParam(defaultValue = "") Integer rangceId,
                            @RequestParam Integer pageNum,
                            @RequestParam Integer pageSize) {
        QueryWrapper<RangceHost> queryWrapper = new QueryWrapper<>();
        if (!"".equals(rangceId)) {
        queryWrapper.like("rangce_id", rangceId);
        }
        return Result.success(rangceHostService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

}

