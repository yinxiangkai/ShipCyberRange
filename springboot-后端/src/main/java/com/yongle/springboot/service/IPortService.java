package com.yongle.springboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yongle.springboot.common.Result;
import com.yongle.springboot.entity.Port;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yongle
 * @since 2023-02-28
 */
public interface IPortService extends IService<Port> {


    Page<Port> findPage(Page<Port> page, Integer networkid,String status,String ipaddr);

    void insertPort(Port port);

    Result delPort(Integer id);
}
