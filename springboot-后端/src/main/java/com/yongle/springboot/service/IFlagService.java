package com.yongle.springboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yongle.springboot.common.Result;
import com.yongle.springboot.entity.Flag;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yongle
 * @since 2023-03-07
 */
public interface IFlagService extends IService<Flag> {

    Result insertFlag(Flag flag);

    Page<Flag> findPage(Page<Flag> page, String rangceName, Integer userId);
}
