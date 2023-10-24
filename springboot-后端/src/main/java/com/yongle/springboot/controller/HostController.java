package com.yongle.springboot.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yongle.springboot.common.Constants;

import com.yongle.springboot.common.StatusEnum;
import com.yongle.springboot.entity.Dashboard;
import com.yongle.springboot.entity.dto.TaskDto;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yongle.springboot.common.Result;
import com.yongle.springboot.service.IHostService;
import com.yongle.springboot.mapper.HostMapper;
import com.yongle.springboot.entity.Host;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yongle
 * @since 2023-03-01
 */
@RestController
@RequestMapping("/host")
public class HostController {

    @Resource
    private IHostService hostService;
    @Resource
    private HostMapper hostMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;


    // 新增或者更新
    @PostMapping("/save")
    public Result save(@RequestBody Host host) {
        if ( host.getId() == null) {
            QueryWrapper<Host> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("name",host.getName());
            queryWrapper.eq("group_id",host.getGroupId());
            if(hostMapper.selectOne(queryWrapper)!=null)
            {
                return Result.error(Constants.CODE_400, "主机名已存在");
            }
            host.setCreatetime(LocalDateTime.now());
            host.setStatus(StatusEnum.Build.toString());
            hostMapper.insertGetId(host);
            TaskDto taskDto=new TaskDto();
            Integer taskid= this.TaskRedis();
            hostService.insertHost(host,taskid);
            taskDto.setId(taskid);
            taskDto.setStatus(StatusEnum.UNFINISHED.toString());
        return Result.success(taskDto,taskid+"号创建主机任务提交成功");
        }else{
         hostMapper.updateById(host);
        return Result.success(null,"更新成功");
        }
    }

    @DeleteMapping("/del/{id}")
    public Result delete(@PathVariable Integer id) {
        hostService.delHost(id);
        return Result.success(null,"删除成功");
    }


    @GetMapping("/page")
    public Result findPage( @RequestParam(defaultValue = "") String name,
                            @RequestParam(defaultValue = "") String userName,
                            @RequestParam(defaultValue = "") String rangceName,
                            @RequestParam(defaultValue = "") String subnetName,
                            @RequestParam(defaultValue = "") String ipaddr,
                            @RequestParam(defaultValue = "") String no,
                            @RequestParam Integer pageNum,
                            @RequestParam Integer pageSize) {
        return Result.success(hostService.findpage(new Page<>(pageNum, pageSize), name,userName,rangceName,no,subnetName,ipaddr));
    }
    @GetMapping("/page2")
    public Result findPage2( @RequestParam(defaultValue = "") String name,
                             @RequestParam Integer rangceId,
                             @RequestParam Integer groupId,
                            @RequestParam Integer pageNum,
                            @RequestParam Integer pageSize) {
        QueryWrapper<Host> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("rangce_id",rangceId);
        queryWrapper.eq("group_id",groupId);
        queryWrapper.like("name",name);

        return Result.success(hostService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }
    @GetMapping("/list")
    public Result findList( @RequestParam Integer rangceId,
                            @RequestParam Integer userId,
                            @RequestParam(defaultValue = "") Integer type) {
        QueryWrapper<Host> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("rangce_id",rangceId);
        queryWrapper.eq("user_id",userId);
        if (!"".equals(type)) {
            queryWrapper.like("type", type);
        }
        return Result.success(hostMapper.selectList(queryWrapper));
    }

    @GetMapping("/status/{id}")
    public Result getStatus( @PathVariable Integer id){


        return Result.success(hostMapper.selectById(id).getStatus());
    }

    @GetMapping("/stop/{id}")
    public Result stop(@PathVariable Integer id){
        Host host= hostMapper.selectById(id);
        host.setStatus(StatusEnum.Shuting.toString());
        hostMapper.updateById(host);
        TaskDto taskDto=new TaskDto();
        Integer taskid= this.TaskRedis();
        hostService.stopHost(taskid,id);
        taskDto.setId(taskid);
        taskDto.setStatus(StatusEnum.UNFINISHED.toString());
        return Result.success(taskDto,taskid+"号关机任务提交成功");
    }
    @GetMapping("/start/{id}")
    public Result start(@PathVariable Integer id){
        Host host= hostMapper.selectById(id);
        host.setStatus(StatusEnum.Starting.toString());
        hostMapper.updateById(host);
        TaskDto taskDto=new TaskDto();
        Integer taskid= this.TaskRedis();
        hostService.startHost(taskid,id);
        taskDto.setId(taskid);
        taskDto.setStatus(StatusEnum.UNFINISHED.toString());
        return Result.success(taskDto,taskid+"号开机任务提交成功");
    }
    @GetMapping("/use/{id}")
    public Result geturl( @PathVariable Integer id){
        return hostService.geturl(id);
    }
    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable Integer id){

        return Result.success(hostService.findone(id),"获得一个主机的详细信息");
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
    @GetMapping("/getNumber")
    public Result getNumber() {
        return Result.success(hostService.count());
    }
    @GetMapping("/getUseNumber")
    public Result getUseNumber() {
        Dashboard dashboard;
        return Result.success(hostService.count());
    }
}

