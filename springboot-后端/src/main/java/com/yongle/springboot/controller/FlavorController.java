package com.yongle.springboot.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yongle.springboot.common.Constants;
import com.yongle.springboot.mapper.FlavorMapper;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.net.MalformedURLException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yongle.springboot.common.Result;

import com.yongle.springboot.service.IFlavorService;
import com.yongle.springboot.entity.Flavor;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yongle
 * @since 2023-02-22
 */
@RestController
@RequestMapping("/flavor")
public class FlavorController {

    @Resource
    private IFlavorService flavorService;
    @Resource
    private FlavorMapper flavorMapper;

    // 新增或者更新
    @GetMapping("/page")
    public Result findPage(@RequestParam(defaultValue = "") String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Flavor> queryWrapper = new QueryWrapper<>();
        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }
        return Result.success(flavorService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }
    @DeleteMapping("/del/{id}")
    public Result delete(@PathVariable String id) {
        flavorService.delFlavor(id);
        return Result.success(null,"删除成功");
    }
    @PostMapping("/save")
    public Result save(@RequestBody Flavor flavor) throws MalformedURLException {
        if(flavor.getRam()<512)
        {
            return Result.error(Constants.CODE_400,"内存低于512MB");
        }else if(flavor.getDisk()<1)
        {
            return Result.error(Constants.CODE_400,"磁盘低于1GB");
        }else if(flavor.getVcpus()<1)
        {
            return Result.error(Constants.CODE_400,"至少选择一个cpu");
        }
        if (flavor.getId() == null) {
            QueryWrapper<Flavor> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("name", flavor.getName());
            if(flavorMapper.selectOne(queryWrapper)!=null)
            {
                return Result.error(Constants.CODE_400, "实例类型名已存在");
            }
            flavorService.insertFlavor(flavor);
            return Result.success(null,"新增成功");
        }else{
            flavorMapper.updateById(flavor);
            return Result.success(null,"更新成功");
        }

    }

}

