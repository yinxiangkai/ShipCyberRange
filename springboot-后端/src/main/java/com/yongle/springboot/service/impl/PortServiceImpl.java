package com.yongle.springboot.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yongle.springboot.common.Constants;
import com.yongle.springboot.common.Result;
import com.yongle.springboot.entity.Port;
import com.yongle.springboot.mapper.NetworkMapper;
import com.yongle.springboot.mapper.PortMapper;
import com.yongle.springboot.mapper.SubnetMapper;
import com.yongle.springboot.service.IPortService;
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
 * @since 2023-02-28
 */
@Service
public class PortServiceImpl extends ServiceImpl<PortMapper, Port> implements IPortService {

    @Resource
    private PortMapper portMapper;
    @Resource
    private SubnetMapper subnetMapper;
    @Resource
    private NetworkMapper networkMapper;

    @Override
    public Page<Port> findPage(Page<Port> page, Integer networkid,String status,String ipaddr) {

        return portMapper.findPage(page,networkid,status,ipaddr);
    }

    @Override
    public void insertPort(Port port) {
        String name=port.getName();
        String networkId=networkMapper.selectById(port.getNetworkId()).getNetworkId();
        String ipaddr=port.getIpaddr();
        String subnetId=subnetMapper.selectById(port.getSubnetId()).getSubnetid();
        org.openstack4j.model.network.Port opport;
        OpenstackUtils openstackutil = new OpenstackUtils();

        opport=openstackutil.createport(name,networkId,ipaddr,subnetId);
        port.setStatus(opport.getState().toString());
        port.setPort(opport.getId());
        portMapper.insert(port);
    }

    @Override
    public Result delPort(Integer id) {
        if(portMapper.selectById(id).getStatus().equals("Active")){
            return Result.error(Constants.CODE_600,"该端口正在使用中");
        }
        OpenstackUtils openstackutil = new OpenstackUtils();
        openstackutil.delport(portMapper.selectById(id).getPort());
        this.removeById(id);
        return Result.success(null,"该端口正在使用中");
    }
}
