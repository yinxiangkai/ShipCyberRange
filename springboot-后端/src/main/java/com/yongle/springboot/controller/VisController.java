package com.yongle.springboot.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yongle.springboot.entity.dto.VisData;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yongle.springboot.common.Result;

import com.yongle.springboot.service.IVisService;
import com.yongle.springboot.mapper.VisMapper;
import com.yongle.springboot.entity.Vis;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yongle
 * @since 2023-03-26
 */
@RestController
@RequestMapping("/vis")
public class VisController {

    @Resource
    private IVisService visService;
    @Resource
    private VisMapper visMapper;

    // 新增或者更新
    @PostMapping("/save")
    public Result save(@RequestBody Vis vis) {

        if ( vis.getId() == null) {
         visMapper.insert(vis);
        return Result.success(null,"新增成功");
        }else{
         visMapper.updateById(vis);
        return Result.success(null,"更新成功");
        }
        }

    @DeleteMapping("/del/{id}")
    public Result delete(@PathVariable Integer id) {
        visService.removeById(id);
        return Result.success(null,"删除成功");
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        visService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping("/map")
    public Result getMap( @RequestParam(defaultValue = "") String rangceId,
                            @RequestParam Integer userId) {
        VisData visData=visService.getVisMap(rangceId,userId);
        return Result.success(visData);
    }

    @GetMapping("/page")
    public Result findPage( @RequestParam(defaultValue = "") String rangceId,
                            @RequestParam Integer pageNum,
                            @RequestParam Integer pageSize) {
        QueryWrapper<Vis> queryWrapper = new QueryWrapper<>();
        if (!"".equals(rangceId)) {
        queryWrapper.like("rangce_id", rangceId);
        }
        return Result.success(visService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

}

