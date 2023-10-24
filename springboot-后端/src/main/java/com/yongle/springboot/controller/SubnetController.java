package com.yongle.springboot.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yongle.springboot.common.Constants;
import com.yongle.springboot.common.MyFunction;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yongle.springboot.common.Result;

import com.yongle.springboot.service.ISubnetService;
import com.yongle.springboot.mapper.SubnetMapper;
import com.yongle.springboot.entity.Subnet;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yongle
 * @since 2023-02-27
 */
@RestController
@RequestMapping("/subnet")
public class SubnetController {

    @Resource
    private ISubnetService subnetService;
    @Resource
    private SubnetMapper subnetMapper;

    // 新增或者更新
    @PostMapping("/save")
    public Result save(@RequestBody Subnet subnet) {
        if(!MyFunction.isCidr(subnet.getCidr())){
            return Result.error(Constants.CODE_400, "CIDR格式错误");
        }
        if ( subnet.getId() == null) {
            QueryWrapper<Subnet> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("cidr",subnet.getCidr());
            queryWrapper.eq("networkid",subnet.getNetworkid());
            if(subnetMapper.selectOne(queryWrapper)!=null)
            {
                return Result.error(Constants.CODE_400, "CIDR已存在");
            }
            QueryWrapper<Subnet> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("name",subnet.getName());
            queryWrapper1.eq("networkid",subnet.getNetworkid());
            if(subnetMapper.selectOne(queryWrapper1)!=null)
            {
                return Result.error(Constants.CODE_400, "子网名已存在");
            }
            subnetService.insertSubnet(subnet);
        return Result.success(null,"新增成功");
        }else{
         subnetMapper.updateById(subnet);
        return Result.success(null,"更新成功");
        }
        }

    @DeleteMapping("/del/{id}")
    public Result delete(@PathVariable Integer id) {

        return subnetService.delSubnet(id);
    }

    @GetMapping("/page")
    public Result findPage( @RequestParam Integer networkid,
                            @RequestParam Integer pageNum,
                            @RequestParam Integer pageSize) {
        QueryWrapper<Subnet> queryWrapper = new QueryWrapper<>();
        if (!"".equals(networkid)) {
        queryWrapper.like("networkid",networkid);
        }
        return Result.success(subnetService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

}

