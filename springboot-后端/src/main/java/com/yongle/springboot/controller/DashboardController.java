package com.yongle.springboot.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yongle.springboot.common.Result;

import com.yongle.springboot.service.IDashboardService;
import com.yongle.springboot.mapper.DashboardMapper;
import com.yongle.springboot.entity.Dashboard;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yongle
 * @since 2023-04-13
 */
@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Resource
    private IDashboardService dashboardService;
    @Resource
    private DashboardMapper dashboardMapper;

    // 新增或者更新
    @PostMapping("/save")
    public Result save(@RequestBody Dashboard dashboard) {

        if ( dashboard.getId() == null) {
         dashboardMapper.insert(dashboard);
        return Result.success(null,"新增成功");
        }else{
         dashboardMapper.updateById(dashboard);
        return Result.success(null,"更新成功");
        }
        }

    @DeleteMapping("/del/{id}")
    public Result delete(@PathVariable Integer id) {
        dashboardService.removeById(id);
        return Result.success(null,"删除成功");
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        dashboardService.removeByIds(ids);
        return Result.success();
    }



    @GetMapping("/getOne")
    public Result getOne( ) {
        QueryWrapper<Dashboard> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", 1);

        return Result.success(dashboardService.getOne(queryWrapper));
    }
    @GetMapping("/getUseNumber")
    public Result getUseNumber() {
        return Result.success(dashboardMapper.getUseNumber());
    }
}

