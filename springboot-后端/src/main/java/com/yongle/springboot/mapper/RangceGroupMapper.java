package com.yongle.springboot.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yongle.springboot.entity.RangceGroup;
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
 * @since 2023-02-28
 */
@Mapper
public interface RangceGroupMapper extends BaseMapper<RangceGroup> {

    Page<RangceGroup> findpage(Page<RangceGroup> page, @Param("no") String no, @Param("rangce_id") Integer rangceId,@Param("userId") Integer userId, @Param("rangceName")String rangceName);

    RangceGroup findOne( @Param("rangceId")Integer rangceId);

    List<Integer> selectmyList( @Param("rangceId")Integer rangceId);

    void insertGetId(RangceGroup rangceGroup1);
}
