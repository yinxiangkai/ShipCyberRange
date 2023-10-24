package com.yongle.springboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yongle.springboot.common.Result;
import com.yongle.springboot.entity.RangceGroup;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yongle
 * @since 2023-02-28
 */
public interface IRangceGroupService extends IService<RangceGroup> {

    Page<RangceGroup> findpage(Page<RangceGroup> page, String no, Integer rangceId,Integer userId,String rangceName);



    Result abandon(Integer groupId);

    void apply(Integer uid, Integer rangceId, Integer taskid,Integer groupId);
}
