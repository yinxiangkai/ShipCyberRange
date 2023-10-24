package com.yongle.springboot.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yongle.springboot.entity.Rangce;
import com.yongle.springboot.mapper.RangceMapper;
import com.yongle.springboot.service.IPortService;
import com.yongle.springboot.service.IRangceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yongle
 * @since 2023-02-24
 */
@Service
public class RangceServiceImpl extends ServiceImpl<RangceMapper, Rangce> implements IRangceService {

    @Resource
    private RangceMapper rangceMapper;
    @Override
    public Page<Rangce> findPage(Page<Rangce> page, String name) {

        return  rangceMapper.findPage(page,name);
    }
}
