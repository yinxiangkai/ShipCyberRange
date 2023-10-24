package com.yongle.springboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yongle.springboot.common.Result;
import com.yongle.springboot.entity.Host;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yongle
 * @since 2023-03-02
 */
public interface IHostService extends IService<Host> {

    void insertHost(Host host,Integer taskid);

    void delHost(Integer id);

    Page<Host> findpage(Page<Host> page, String name, String userName, String rangceName, String no, String subnetName, String ipaddr);

    void stopHost(Integer taskid, Integer id);

    void startHost(Integer taskid, Integer id);

    Result geturl(Integer id);

    Object findone(Integer id);
}
