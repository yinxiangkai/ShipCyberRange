package com.yongle.springboot.service;

import com.yongle.springboot.entity.Flavor;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yongle
 * @since 2023-02-23
 */
public interface IFlavorService extends IService<Flavor> {

    void delFlavor(String id);

    void insertFlavor(Flavor flavor);
}
