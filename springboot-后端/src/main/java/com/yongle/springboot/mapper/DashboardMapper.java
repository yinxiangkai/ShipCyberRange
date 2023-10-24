package com.yongle.springboot.mapper;

import com.yongle.springboot.entity.Dashboard;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yongle
 * @since 2023-04-13
 */
@Mapper
public interface DashboardMapper extends BaseMapper<Dashboard> {

    Dashboard getUseNumber();


    Dashboard getAddNumber(@Param("rangceId") Integer rangceId);
}
