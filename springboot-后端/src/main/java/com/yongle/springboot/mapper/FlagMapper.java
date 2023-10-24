package com.yongle.springboot.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yongle.springboot.entity.Flag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yongle
 * @since 2023-03-07
 */
@Mapper
public interface FlagMapper extends BaseMapper<Flag> {

    Page<Flag> findPage(Page<Flag> page, @Param("rangceName") String rangceName, @Param("userId") Integer userId);
}
