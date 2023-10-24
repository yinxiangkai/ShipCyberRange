package com.yongle.springboot.service;

import com.yongle.springboot.common.Result;
import com.yongle.springboot.entity.Subnet;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yongle
 * @since 2023-02-27
 */
public interface ISubnetService extends IService<Subnet> {

    void insertSubnet(Subnet subnet);

    Result delSubnet(Integer id);
}
