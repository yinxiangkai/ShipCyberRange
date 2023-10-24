package com.yongle.springboot.mapper;

import com.yongle.springboot.entity.RangceHost;
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
 * @since 2023-04-16
 */
@Mapper
public interface RangceHostMapper extends BaseMapper<RangceHost> {

    List<RangceHost> selectRangceHostList(@Param("rangceId")String rangceId, @Param("userId") Integer userId);

    List<RangceHost> selectDownList(String rangceId, Integer userId);
}
