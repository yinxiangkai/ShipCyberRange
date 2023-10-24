package com.yongle.springboot.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yongle.springboot.entity.Port;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yongle
 * @since 2023-02-28
 */
@Mapper
public interface PortMapper extends BaseMapper<Port> {

    Page<Port> findPage(Page<Port> page, @Param("networkid") Integer networkid,@Param("status") String status,@Param("ipaddr") String ipaddr);

    void insertGetId(Port port);
}
