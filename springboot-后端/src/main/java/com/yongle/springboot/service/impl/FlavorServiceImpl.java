package com.yongle.springboot.service.impl;

import com.yongle.springboot.entity.Flavor;
import com.yongle.springboot.mapper.FlavorMapper;
import com.yongle.springboot.service.IFlavorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yongle.springboot.utils.OpenstackUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yongle
 * @since 2023-02-23
 */
@Service
public class FlavorServiceImpl extends ServiceImpl<FlavorMapper, Flavor> implements IFlavorService {
    @Resource
    private FlavorMapper flavorMapper;
    @Override
    public void delFlavor(String id) {
        OpenstackUtils openstackUtils = new OpenstackUtils();
        openstackUtils.delflavor(flavorMapper.selectById(id).getValue());
        this.removeById(id);
    }

    @Override
    public void insertFlavor(Flavor flavor) {
        OpenstackUtils openstackUtils = new OpenstackUtils();
        flavor.setValue(openstackUtils.createFlavor(flavor));
        flavorMapper.insert(flavor);
    }
}
