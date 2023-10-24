package com.yongle.springboot.mapper;

import com.yongle.springboot.entity.Vis;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yongle
 * @since 2023-03-26
 */
@Mapper
public interface VisMapper extends BaseMapper<Vis> {

    List<Vis> selectVisList(@Param("rangceId")String rangceId,@Param("userId") Integer userId);
}
