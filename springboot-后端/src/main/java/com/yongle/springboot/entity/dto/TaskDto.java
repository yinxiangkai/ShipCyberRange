package com.yongle.springboot.entity.dto;

import com.yongle.springboot.common.StatusEnum;
import lombok.Data;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.util.Objects;

@Data
public class TaskDto {
    @Resource
    StringRedisTemplate stringRedisTemplate ;
    Integer id;
    String status;


}
