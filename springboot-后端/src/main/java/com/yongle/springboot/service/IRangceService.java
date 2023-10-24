package com.yongle.springboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yongle.springboot.entity.Rangce;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yongle
 * @since 2023-02-24
 */
public interface IRangceService extends IService<Rangce> {

    Page<Rangce> findPage(Page<Rangce> page, String name);
}
