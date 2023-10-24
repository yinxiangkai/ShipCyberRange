package com.yongle.springboot.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yongle.springboot.common.Constants;
import com.yongle.springboot.mapper.NetworkMapper;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yongle.springboot.common.Result;
import com.yongle.springboot.service.INetworkService;
import com.yongle.springboot.entity.Network;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yongle
 * @since 2023-02-26
 */
@RestController
@RequestMapping("/network")
public class NetworkController {

    @Resource
    private INetworkService networkService;
    @Resource
    private NetworkMapper networkMapper;
    @GetMapping("/page")
    public Result findPage(@RequestParam(defaultValue = "") String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Network> queryWrapper = new QueryWrapper<>();
        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }
        return Result.success(networkService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }
    @DeleteMapping("/del/{id}")
    public Result delete(@PathVariable Integer id) {
        return  networkService.delNetwork(id);
    }
    @PostMapping("/save")
    public Result save(@RequestBody Network network) {
        if (network.getId() == null) {
            QueryWrapper<Network> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("name", network.getName());
            if(networkMapper.selectOne(queryWrapper)!=null)
            {
                return Result.error(Constants.CODE_400, "网络名已存在");
            }
            networkService.insertNetwork(network);
            return Result.success(null,"新增成功");
        }else{
            networkMapper.updateById(network);
            return Result.success(null,"更新成功");
        }
    }


}

