package com.yongle.springboot.task;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yongle.springboot.entity.RangceGroup;
import com.yongle.springboot.mapper.RangceGroupMapper;
import com.yongle.springboot.service.IRangceGroupService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class ResourceReleaseTask {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private IRangceGroupService rangceGroupService;
    @Resource
    private RangceGroupMapper rangceGroupMapper;
    @Scheduled(fixedRate = 60000) //ms
    public void releaseExpiredResources() {
        String jsonStr = stringRedisTemplate.opsForValue().get("Group_key");
        List<RangceGroup> rangceGroupList;
        if (StrUtil.isBlank(jsonStr)) {
            QueryWrapper<RangceGroup> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("status","Active");
            rangceGroupList =  rangceGroupMapper.selectList(queryWrapper);
            stringRedisTemplate.opsForValue().set("Group_key", JSONUtil.toJsonStr(rangceGroupList));
        } else {
            rangceGroupList = JSONUtil.toBean(jsonStr, new TypeReference<List<RangceGroup>>() {
            }, true);
        }
        if(!rangceGroupList.isEmpty()){
            for(RangceGroup rangceGroup:rangceGroupList){
                if(rangceGroup.getEndTime().isBefore(LocalDateTime.now())){
                    rangceGroupService.abandon(rangceGroup.getId());
                    rangceGroupMapper.deleteById(rangceGroup);
                    stringRedisTemplate.delete("Group_key");
                }
            }
        }
    }


}
