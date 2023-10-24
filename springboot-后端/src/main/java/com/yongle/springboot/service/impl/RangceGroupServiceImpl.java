package com.yongle.springboot.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yongle.springboot.common.Constants;
import com.yongle.springboot.common.Result;
import com.yongle.springboot.common.StatusEnum;
import com.yongle.springboot.entity.*;
import com.yongle.springboot.exception.ServiceException;
import com.yongle.springboot.mapper.*;
import com.yongle.springboot.service.INetworkService;
import com.yongle.springboot.service.IRangceGroupService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yongle.springboot.utils.OpenstackUtils;
import org.openstack4j.model.compute.Server;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yongle
 * @since 2023-02-28
 */
@Service
public class RangceGroupServiceImpl extends ServiceImpl<RangceGroupMapper, RangceGroup> implements IRangceGroupService {

    @Resource
    private RangceGroupMapper rangceGroupMapper;
    @Resource
    private RangceHostMapper rangceHostMapper;
    @Resource
    private NetworkMapper networkMapper;
    @Resource
    private RangceMapper rangceMapper;
    @Resource
    private SubnetMapper subnetMapper;
    @Resource
    private PortMapper portMapper;
    @Resource
    private FlavorMapper flavorMapper;
    @Resource
    private ImgMapper imgMapper;
    @Resource
    private HostMapper hostMapper;
    @Resource
    private INetworkService networkService;
    private static final Log LOG = Log.get();

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public Page<RangceGroup> findpage(Page<RangceGroup> page, String no, Integer rangceId,Integer userId,String rangceName) {

        return rangceGroupMapper.findpage(page,no,rangceId,userId, rangceName);

    }
    @Override
    @Async("asyncExecutor")
    public void apply(Integer uid, Integer rangceId, Integer taskid,Integer groupId) {
        Rangce rangce=rangceMapper.selectById(rangceId);
        QueryWrapper<RangceHost> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("rangce_id",rangceId);
        List<RangceHost> rangceHostList = rangceHostMapper.selectList(queryWrapper);
        Network network=this.createNetwork(uid,rangceId);
        Subnet subnet =this.createSubnet(uid,rangceId,network.getId(),rangce.getCidr(),network.getNetworkId());
        for(RangceHost rangceHost :rangceHostList){
            if(rangceHost.getType()==1){
                Port port=this.createPort(uid,rangceId,network.getId(),subnet.getId(),rangceHost.getIpaddr(),rangceHost.getNumber(),network.getNetworkId(),subnet.getSubnetid());
                this.createHost(rangceHost,network,subnet,port,uid,rangceId,groupId);
            }
        }
        RangceGroup rangceGroup=rangceGroupMapper.selectById(groupId);
        rangceGroup.setStatus(StatusEnum.Active.toString());
        rangceGroup.setApplyTime(LocalDateTime.now());
        rangceGroup.setEndTime(rangceGroup.getApplyTime().plus(rangce.getUseTime().toNanoOfDay(), ChronoUnit.NANOS));
        rangceGroupMapper.updateById(rangceGroup);
        stringRedisTemplate.delete("Group_key");
        stringRedisTemplate.opsForValue().set(taskid.toString(),StatusEnum.FINISHED.toString());
    }




    private Network createNetwork(Integer uid, Integer rangceId) {
        String name="network_"+Integer.toString(rangceId)+"_"+Integer.toString(uid);
        OpenstackUtils openstackUtil = new OpenstackUtils();
        org.openstack4j.model.network.Network opnetwork=openstackUtil.createNetwork(name);
        Network network=new Network();
        network.setTime(LocalDateTime.now());
        network.setStatus(opnetwork.getStatus().toString());
        network.setNetworkId(opnetwork.getId());
        network.setName(opnetwork.getName());
        networkMapper.insertGetId(network);
        return network;

    }
    private Subnet createSubnet(Integer uid, Integer rangceId,Integer networkId,String CIDR,String oNetworkId) {
        OpenstackUtils openstackUtil = new OpenstackUtils();
        Subnet subnet =new Subnet();
        String name="subnet_"+Integer.toString(rangceId)+"_"+Integer.toString(uid);
        subnet.setName(name);
        subnet.setNetworkid(networkId);
        subnet.setCidr(CIDR);
        subnet.setNetwork(oNetworkId);
        subnet.setNetwork(networkMapper.selectById(subnet.getNetworkid()).getNetworkId());
        org.openstack4j.model.network.Subnet opsubnet;
        opsubnet=openstackUtil.createSubnet(subnet);
        subnet.setSubnetid(opsubnet.getId());
        subnet.setGateway(opsubnet.getGateway());
        subnet.setVersion(opsubnet.getIpVersion().toString());
        subnetMapper.insertGetId(subnet);
        return subnet;
    }
    private Port createPort(Integer uid, Integer rangceId,Integer networkId,Integer subnetId,String ipaddr,Integer number,String network,String subnet){
        OpenstackUtils openstackUtil = new OpenstackUtils();
        Port port=new Port();
        String name="port_"+Integer.toString(rangceId)+"_"+Integer.toString(uid)+"_"+Integer.toString(number);
        port.setName(name);
        port.setIpaddr(ipaddr);
        port.setNetworkId(networkId);
        port.setSubnetId(subnetId);
        org.openstack4j.model.network.Port opport=openstackUtil.createport(name,network,ipaddr,subnet);
        port.setStatus(opport.getState().toString());
        port.setPort(opport.getId());
        portMapper.insertGetId(port);
        return port;
    }
    private void createHost(RangceHost rangceHost,Network network,Subnet subnet,Port port,Integer uid,Integer rangceId,Integer groupId){
        String name=rangceHost.getName()+"_"+Integer.toString(rangceId)+"_"+Integer.toString(uid)+"_"+Integer.toString(rangceHost.getNumber());
        Host host=new Host();
        host.setCreatetime(LocalDateTime.now());
        host.setName(name);
        if(rangceHost.getStatus())
        {
            host.setType(0);
        }else{
            host.setType(1);
        }
        host.setGrade(rangceHost.getGrade());
        host.setGroupId(groupId);
        host.setRangceId(rangceId);
        host.setFlag(rangceHost.getFlag());
        host.setPortId(port.getId());
        host.setFlavorId(rangceHost.getFlavorId());
        host.setImageId(rangceHost.getImageId());
        host.setNumber(rangceHost.getNumber());
        host.setUserId(uid);
        host.setSubnetId(rangceHost.getRangceId());
        String flavor=flavorMapper.selectById(host.getFlavorId()).getValue();
        String image=imgMapper.selectById(host.getImageId()).getValue();
        OpenstackUtils openstackUtils =new OpenstackUtils();
        Server service = openstackUtils.createservice(name,flavor , image,port.getPort());
        host.setIpaddr(port.getIpaddr());
        host.setSubnetId(port.getSubnetId());
        port.setStatus("Active");
        portMapper.updateById(port);
        host.setHost(service.getId());
        host.setStatus(openstackUtils.bulid(host.getHost()));
        host.setUrl(openstackUtils.geturl(host.getHost()));
        hostMapper.insert(host);
    }


    @Override
    @Async("asyncExecutor")
    public Result abandon(Integer groupId) {
        QueryWrapper<Host> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("group_id",groupId);
        List<Host> hostList = hostMapper.selectList(queryWrapper);
        Integer networkId=null;
        if(!hostList.isEmpty()){
            for(Host host:hostList){
                Port port =portMapper.selectById(host.getPortId());
                port.setStatus("DOWN");
                networkId=port.getNetworkId();
                portMapper.updateById(port);
                OpenstackUtils openstackUtils = new OpenstackUtils();
                try{
                    openstackUtils.delhost(host.getHost());
                    hostMapper.deleteById(host);
                }catch (NullPointerException e){
                    LOG.error(e);
                    throw new ServiceException(Constants.CODE_500, "主机创建中");
                }
            }
            networkService.delNetwork(networkId);
        }
        return Result.success(null,"成功释放靶场");
    }



    public Integer TaskRedis(){
        String jsonStr = stringRedisTemplate.opsForValue().get("task");
        if (StrUtil.isBlank(jsonStr)) {
            stringRedisTemplate.opsForValue().set("task","0");
        }
        Integer taskid=Integer.parseInt(stringRedisTemplate.opsForValue().get("task"));
        taskid++;
        stringRedisTemplate.opsForValue().set("task",taskid.toString());
        stringRedisTemplate.opsForValue().set(taskid.toString(), StatusEnum.UNFINISHED.toString());
        return taskid;
    }
}
