package com.yongle.springboot.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yongle.springboot.entity.Host;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yongle
 * @since 2023-03-02
 */
@Mapper
public interface HostMapper extends BaseMapper<Host> {

    void insertGetId(Host host);

    Page<Host> findpage(Page<Host> page, @Param("name")String name, @Param("userName")String userName, @Param("rangceName")String rangceName, @Param("no")String no,@Param("subnetName")String subnetName, @Param("ipaddr")String ipaddr);

    Object findone(@Param("id")Integer id);
}