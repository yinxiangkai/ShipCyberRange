package com.yongle.springboot.service;

import com.yongle.springboot.entity.Vis;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yongle.springboot.entity.dto.VisData;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yongle
 * @since 2023-03-26
 */
public interface IVisService extends IService<Vis> {

    VisData getVisMap(String rangceId, Integer userId);
}
