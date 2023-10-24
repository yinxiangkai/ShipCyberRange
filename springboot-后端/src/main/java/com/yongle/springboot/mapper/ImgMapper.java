package com.yongle.springboot.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yongle.springboot.entity.Img;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yongle
 * @since 2023-02-22
 */
@Mapper
public interface ImgMapper extends BaseMapper<Img> {

    Page<Img> findPage(Page<Img> page, @Param("name") String name, @Param("status") String status);
}
