package com.yongle.springboot.mapper;

import com.yongle.springboot.entity.Subnet;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yongle
 * @since 2023-02-27
 */
@Mapper
public interface SubnetMapper extends BaseMapper<Subnet> {

    Subnet getOne(@Param("rangceId")String rangceId, @Param("userId") Integer userId);

    void insertGetId(Subnet subnet);
}
