package com.yongle.springboot.mapper;

import com.yongle.springboot.entity.Network;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yongle
 * @since 2023-02-26
 */
@Mapper
public interface NetworkMapper extends BaseMapper<Network> {

    void insertGetId(Network network);
}
