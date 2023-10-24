package com.yongle.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yongle.springboot.common.Constants;
import com.yongle.springboot.common.Result;
import com.yongle.springboot.entity.Network;
import com.yongle.springboot.entity.Port;
import com.yongle.springboot.mapper.NetworkMapper;
import com.yongle.springboot.mapper.PortMapper;
import com.yongle.springboot.service.INetworkService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yongle.springboot.service.IPortService;
import com.yongle.springboot.utils.OpenstackUtils;
import org.openstack4j.api.networking.PortService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yongle
 * @since 2023-02-26
 */
@Service
public class NetworkServiceImpl extends ServiceImpl<NetworkMapper, Network> implements INetworkService {

    @Resource
    private NetworkMapper networkMapper;
    @Resource
    private PortMapper portMapper;
    @Resource
    private IPortService portService;
    @Override
    public Result delNetwork(Integer id) {
        QueryWrapper<Port> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("network_id",id);
        queryWrapper.eq("status","Active");
        if(portMapper.selectOne(queryWrapper)!=null){
            return Result.error(Constants.CODE_600,"删除失败，该网络存在端口处于使用状态");
        }
        QueryWrapper<Port> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("network_id",id);
        List<Port>ports= portMapper.selectList(queryWrapper1);
        if(ports!=null)
        {
          for(Port port: ports){
              portService.delPort(port.getId());
          }
        }
        OpenstackUtils openstackutil = new OpenstackUtils();
        openstackutil.delnetwork(networkMapper.selectById(id).getNetworkId());
        this.removeById(id);
        return Result.success(null,"删除成功");
    }

    @Override
    public void insertNetwork(Network network) {
        network.setTime(LocalDateTime.now());
        org.openstack4j.model.network.Network opnetwork;
        OpenstackUtils openstackutil = new OpenstackUtils();
        opnetwork= openstackutil.createnetwork(network.getName());
        network.setStatus(opnetwork.getStatus().toString());
        network.setNetworkId(opnetwork.getId());
        networkMapper.insert( network);
    }
}
