package com.yongle.springboot.controller;


import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yongle.springboot.common.Constants;
import com.yongle.springboot.common.MyFunction;
import com.yongle.springboot.common.Result;
import com.yongle.springboot.common.StatusEnum;
import com.yongle.springboot.entity.Dashboard;
import com.yongle.springboot.entity.Rangce;
import com.yongle.springboot.entity.RangceGroup;
import com.yongle.springboot.entity.dto.TaskDto;
import com.yongle.springboot.mapper.DashboardMapper;
import com.yongle.springboot.mapper.RangceGroupMapper;
import com.yongle.springboot.mapper.RangceMapper;
import com.yongle.springboot.service.IRangceGroupService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yongle
 * @since 2023-02-28
 */
@RestController
@RequestMapping("/rangce-group")
public class RangceGroupController {

    @Resource
    private IRangceGroupService rangceGroupService;
    @Resource
    private RangceGroupMapper rangceGroupMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private DashboardMapper dashboardMapper;

    // 新增或者更新
    @PostMapping("/save")
    public Result save(@RequestBody RangceGroup rangceGroup) {

        if ( rangceGroup.getId() == null) {
            QueryWrapper<RangceGroup> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("no", rangceGroup.getNo());
            queryWrapper.eq("rangce_id", rangceGroup.getRangceId());
            if(rangceGroupMapper.selectOne(queryWrapper)!=null)
            {
                return Result.error(Constants.CODE_400, "主机组编号重复");
            }
            rangceGroup.setUserId(0);
         rangceGroupMapper.insert(rangceGroup);
         stringRedisTemplate.delete("Group_key");
        return Result.success(null,"新增成功");
        }else{
         rangceGroupMapper.update(rangceGroup,null);
         stringRedisTemplate.delete("Group_key");
        return Result.success(null,"更新成功");
        }
    }

    @DeleteMapping("/del/{id}")
    public Result delete(@PathVariable Integer id) {
        rangceGroupService.removeById(id);
        stringRedisTemplate.delete("Group_key");
        return Result.success(null,"删除成功");
    }


    @GetMapping("/page")
    public Result findPage( @RequestParam(defaultValue = "") String no,
                            @RequestParam(defaultValue = "") Integer userId,
                            @RequestParam(defaultValue = "") String rangceName,
                            @RequestParam Integer pageNum,
                            @RequestParam (defaultValue = "")Integer rangce_id,
                            @RequestParam Integer pageSize) {
          return Result.success(rangceGroupService.findpage(new Page<>(pageNum, pageSize), no,rangce_id,userId,rangceName));
    }
    @GetMapping("/role")
    public Result getRole(  @RequestParam Integer uid,
                            @RequestParam Integer rangceId) {
        QueryWrapper<RangceGroup> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",uid);
        queryWrapper.eq("rangce_id",rangceId);
        RangceGroup rangceGroup=rangceGroupMapper.selectOne(queryWrapper);
        if(rangceGroup!=null)
        {
            if(rangceGroup.getStatus().equals("Active")){
                return Result.success(1, "拥有权限");
            }else{
                return Result.success(2, "申请中");
            }


        }else{
            return Result.success(0,"没有权限");
        }

    }
    @GetMapping("/apply")
    public Result apply(  @RequestParam Integer uid,
                            @RequestParam Integer rangceId) {
        QueryWrapper<RangceGroup> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",uid);
        queryWrapper.eq("rangce_id",rangceId);
        RangceGroup rangceGroup= new RangceGroup();
        if(rangceGroupMapper.selectOne(queryWrapper)!=null)
        {
            return Result.error(Constants.CODE_600, "禁止重复申请靶场资源");
        }else{
            Integer freeCPU;
            Integer freeRam;
            Integer freeDisk;
            Integer addCPU;
            Integer addRam;
            Integer addDisk;
            Dashboard useResource = dashboardMapper.getUseNumber();
            if(useResource==null)
            {
                useResource=new Dashboard();
                useResource.setCpu(0);
                useResource.setDisk(0);
                useResource.setRam(0);
            }
            Dashboard allResource=dashboardMapper.selectById(1);
            freeCPU=allResource.getCpu()-useResource.getCpu();
            freeRam=allResource.getRam()-useResource.getRam();
            freeDisk=allResource.getDisk()-useResource.getDisk();
            Dashboard addResource=dashboardMapper.getAddNumber(rangceId);
            addCPU=addResource.getCpu();
            addRam=addResource.getRam();
            addDisk=addResource.getDisk();
            if(freeCPU<addCPU||freeRam<addRam||freeDisk<addDisk){
                return Result.error(Constants.CODE_600,"靶场资源不足");
            }else{
                RangceGroup rangceGroup1 = new RangceGroup();
                QueryWrapper<RangceGroup> queryWrapper1 = new QueryWrapper<>();
                List<Integer> numberLsit=rangceGroupMapper.selectmyList(rangceId);
                System.out.println(numberLsit);
                if(numberLsit.isEmpty())
                {
                    rangceGroup1.setNo("1");
                }else {
                    rangceGroup1.setNo(MyFunction.findMissingNumber(numberLsit).toString());
                }
                rangceGroup1.setRangceId(rangceId);
                rangceGroup1.setUserId(uid);
                rangceGroup1.setStatus(StatusEnum.Build.toString());
                rangceGroupMapper.insertGetId(rangceGroup1);
                TaskDto taskDto=new TaskDto();
                Integer taskid= this.TaskRedis();
                rangceGroupService.apply(uid,rangceId,taskid,rangceGroup1.getId());
                taskDto.setId(taskid);
                taskDto.setStatus(StatusEnum.UNFINISHED.toString());
                return Result.success(taskDto,"资源申请成功");
            }
        }
    }

    @GetMapping("/abandon")
    public Result abandon(  @RequestParam Integer uid,
                          @RequestParam Integer rangceId) {
        QueryWrapper<RangceGroup> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",uid);
        queryWrapper.eq("rangce_id",rangceId);
        RangceGroup rangceGroup= rangceGroupMapper.selectOne(queryWrapper);
        if(rangceGroup==null)
        {
            return Result.error(Constants.CODE_500, "系统错误");
        }else{
            rangceGroupService.abandon(rangceGroup.getId());
            rangceGroupMapper.deleteById(rangceGroup);
            return Result.success(null,"正在释放资源") ;
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

