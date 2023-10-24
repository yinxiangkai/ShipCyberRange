package com.yongle.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yongle.springboot.common.Constants;
import com.yongle.springboot.common.Result;
import com.yongle.springboot.entity.Port;
import com.yongle.springboot.entity.Subnet;
import com.yongle.springboot.mapper.NetworkMapper;
import com.yongle.springboot.mapper.PortMapper;
import com.yongle.springboot.mapper.SubnetMapper;
import com.yongle.springboot.service.IPortService;
import com.yongle.springboot.service.ISubnetService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yongle.springboot.utils.OpenstackUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yongle
 * @since 2023-02-27
 */
@Service
public class SubnetServiceImpl extends ServiceImpl<SubnetMapper, Subnet> implements ISubnetService {
    @Resource
    private SubnetMapper subnetMapper;
    @Resource
    private NetworkMapper networkMapper;
    @Resource
    private PortMapper portMapper;
    @Resource
    private IPortService portService;

    @Override
    public void insertSubnet(Subnet subnet) {
        subnet.setNetwork(networkMapper.selectById(subnet.getNetworkid()).getNetworkId());
        org.openstack4j.model.network.Subnet opsubnet;
        OpenstackUtils openstackutil = new OpenstackUtils();
        opsubnet=openstackutil.createsubnet(subnet);
        subnet.setSubnetid(opsubnet.getId());
        subnet.setGateway(opsubnet.getGateway());
        subnet.setVersion(opsubnet.getIpVersion().toString());
        subnetMapper.insert(subnet);
    }

    @Override
    public Result delSubnet(Integer id) {
        QueryWrapper<Port> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("subnet_id",id);
        queryWrapper.eq("status","Active");
        if(portMapper.selectOne(queryWrapper)!=null){
            return Result.error(Constants.CODE_600,"删除失败，该网络存在端口处于使用状态");
        }
        QueryWrapper<Port> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("subnet_id",id);
        List<Port> ports= portMapper.selectList(queryWrapper1);
        if(ports!=null)
        {
            for(Port port: ports){
                portService.delPort(port.getId());
            }
        }
        OpenstackUtils openstackutil = new OpenstackUtils();
        openstackutil.delsubnet(subnetMapper.selectById(id).getSubnetid());
        this.removeById(id);
        return Result.success(null,"删除成功");
    }
}
