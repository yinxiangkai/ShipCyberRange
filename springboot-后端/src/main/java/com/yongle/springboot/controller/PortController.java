package com.yongle.springboot.controller;


import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yongle.springboot.common.Constants;
import com.yongle.springboot.common.MyFunction;
import com.yongle.springboot.common.Result;
import com.yongle.springboot.entity.Port;
import com.yongle.springboot.mapper.PortMapper;
import com.yongle.springboot.mapper.SubnetMapper;
import com.yongle.springboot.service.IPortService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yongle
 * @since 2023-02-28
 */
@RestController
@RequestMapping("/port")
public class PortController {
    private static final Log LOG = Log.get();
    @Resource
    private IPortService portService;
    @Resource
    private PortMapper portMapper;
    @Resource
    private SubnetMapper subnetMapper;
    // 新增或者更新
    @PostMapping("/save")
    public Result save(@RequestBody Port port) {
        if(!MyFunction.isValidIPv4(port.getIpaddr()))
        {
            return Result.error(Constants.CODE_400, "IP格式错误");
        }
        if(!MyFunction.matchCIDR(port.getIpaddr(),subnetMapper.selectById(port.getSubnetId()).getCidr()))
        {
            return Result.error(Constants.CODE_400, "IP与CIDR不相符");
        }
        if ( port.getId() == null) {
            QueryWrapper<Port> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("ipaddr",port.getIpaddr());
            queryWrapper.eq("subnet_id",port.getSubnetId());
            if(portMapper.selectOne(queryWrapper)!=null)
            {
                return Result.error(Constants.CODE_400, "IP地址已存在");
            }
            try {
                portService.insertPort(port);
            } catch (Exception e) {
                LOG.error(e);
                return Result.error(Constants.CODE_400, "IP地址已存在");
            }
        return Result.success(null,"新增成功");
        }else{
         portMapper.updateById(port);
        return Result.success(null,"更新成功");
        }
    }

    @DeleteMapping("/del/{id}")
    public Result delete(@PathVariable Integer id) {
        return   portService.delPort(id);
    }

    @GetMapping("/page")
    public Result findPage( @RequestParam(defaultValue="") Integer networkid,
                            @RequestParam(defaultValue = "") String status,
                            @RequestParam(defaultValue = "") String ipaddr,
                            @RequestParam Integer pageNum,
                            @RequestParam Integer pageSize) {
        return Result.success(portService.findPage(new Page<>(pageNum, pageSize), networkid,status,ipaddr));
    }

}

