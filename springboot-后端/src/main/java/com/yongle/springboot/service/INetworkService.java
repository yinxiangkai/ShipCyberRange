package com.yongle.springboot.service;

import com.yongle.springboot.common.Result;
import com.yongle.springboot.entity.Network;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yongle
 * @since 2023-02-26
 */
public interface INetworkService extends IService<Network> {

    Result delNetwork(Integer id);

    void insertNetwork(Network network);
}
