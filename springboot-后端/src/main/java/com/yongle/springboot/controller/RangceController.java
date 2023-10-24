package com.yongle.springboot.controller;


import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yongle.springboot.common.Constants;
import com.yongle.springboot.common.MyFunction;
import com.yongle.springboot.config.AuthAccess;
import com.yongle.springboot.entity.Host;
import com.yongle.springboot.entity.Network;
import com.yongle.springboot.mapper.RangceMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yongle.springboot.common.Result;

import com.yongle.springboot.service.IRangceService;
import com.yongle.springboot.entity.Rangce;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yongle
 * @since 2023-02-24
 */
@RestController
@RequestMapping("/rangce")
public class RangceController {

    @Resource
    private IRangceService rangceService;

    @Resource
    private RangceMapper rangceMapper;
    // 新增或者更新
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @GetMapping("/getNumber")
    public Result getNumber() {
        return Result.success(rangceService.count());
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam(defaultValue = "") String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        return Result.success(rangceService.findPage(new Page<>(pageNum, pageSize), name));
    }
    @PostMapping("/save")
    public Result save(@RequestBody Rangce rangce) {
        if(!MyFunction.isCidr(rangce.getCidr())){
            return Result.error(Constants.CODE_400, "CIDR格式错误");
        }

        if (rangce.getId() == null) {
            QueryWrapper<Rangce> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("name", rangce.getName());
            if(rangceMapper.selectOne(queryWrapper)!=null)
            {
                return Result.error(Constants.CODE_400, "靶场名已存在");
            }
            rangce.setTime(LocalDateTime.now());
            rangce.setAnumber(0);
            rangceMapper.insert(rangce);
            stringRedisTemplate.delete("Rangce_key");
            return Result.success(null,"新增成功");
        }else{
            rangceMapper.updateById(rangce);
            stringRedisTemplate.delete("Rangce_key");
            return Result.success(null,"更改成功");
        }

    }

    @DeleteMapping("/del/{id}")
    public Result delete(@PathVariable Integer id) {
        rangceService.removeById(id);
        stringRedisTemplate.delete("Rangce_key");
        return Result.success();
    }
    @AuthAccess
    @GetMapping("/findAll")
    public Result frontAll() {
        // 1. 从缓存获取数据
        String jsonStr = stringRedisTemplate.opsForValue().get("Rangce_key");
        List<Rangce> rangces;
        if (StrUtil.isBlank(jsonStr)) {  // 2. 取出来的json是空的
            QueryWrapper<Rangce> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("status",true );
            rangces = rangceMapper.selectList(queryWrapper);  // 3. 从数据库取出数据
            // 4. 再去缓存到redis
            stringRedisTemplate.opsForValue().set("Rangce_key", JSONUtil.toJsonStr(rangces));
        } else {
            // 减轻数据库的压力
            // 5. 如果有, 从redis缓存中获取数据
            rangces = JSONUtil.toBean(jsonStr, new TypeReference<List<Rangce>>() {
            }, true);
        }
        return Result.success(rangces);
    }
    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(rangceService.getById(id));
    }
}

