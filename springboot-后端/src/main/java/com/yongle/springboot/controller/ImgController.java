package com.yongle.springboot.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yongle.springboot.common.Constants;
import com.yongle.springboot.common.StatusEnum;
import com.yongle.springboot.entity.dto.TaskDto;
import com.yongle.springboot.mapper.ImgMapper;
import com.yongle.springboot.utils.TokenUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.net.MalformedURLException;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yongle.springboot.common.Result;
import com.yongle.springboot.service.IImgService;
import com.yongle.springboot.entity.Img;
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
@RequestMapping("/img")
public class ImgController {

    @Resource
    private IImgService imgService;
    @Resource
    private ImgMapper imgMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/page")
    public Result findPage(@RequestParam(defaultValue = "") String name,
                           @RequestParam(defaultValue = "") String status,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
           return Result.success(imgService.findPage(new Page<>(pageNum, pageSize), name,status));
    }
    @DeleteMapping("/del/{id}")
    public Result delete(@PathVariable Integer id) {
        imgService.delImg(id);
        return Result.success(null,"删除成功");
    }
    @PostMapping("/save")
    public Result save(@RequestBody Img img) throws MalformedURLException {

        if (img.getId() == null) {
            img.setTime(LocalDateTime.now());
            img.setStatus(StatusEnum.Build.toString());
            TaskDto taskDto=new TaskDto();
            Integer taskid= this.TaskRedis();
            imgMapper.insert(img);
            imgService.insertImg(img,taskid);
            taskDto.setId(taskid);
            taskDto.setStatus(StatusEnum.UNFINISHED.toString());
            return Result.success(taskDto,taskid+"号创建镜像任务提交成功");
        }else{
            imgMapper.updateById(img);
            return Result.success(null,"修改成功");
        }
    }

    public Integer TaskRedis(){
        String jsonStr = stringRedisTemplate.opsForValue().get("task");
        if (StrUtil.isBlank(jsonStr)) {
            stringRedisTemplate.opsForValue().set("task","0");
        }
        Integer taskid=Integer.parseInt(stringRedisTemplate.opsForValue().get("task"));
        taskid++;
        stringRedisTemplate.opsForValue().set("task",taskid.toString());
        stringRedisTemplate.opsForValue().set(taskid.toString(), StatusEnum.UNFINISHED.toString());
        return taskid;
    }
}

