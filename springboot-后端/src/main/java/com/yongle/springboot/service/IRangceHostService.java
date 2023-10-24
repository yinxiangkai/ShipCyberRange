package com.yongle.springboot.service;

import com.yongle.springboot.common.Result;
import com.yongle.springboot.entity.RangceHost;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yongle.springboot.entity.dto.VisData;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yongle
 * @since 2023-04-16
 */
public interface IRangceHostService extends IService<RangceHost> {

    VisData getvisMap(String rangceId, Integer userId);

    Result getDownList(String rangceId, Integer userId);
}
