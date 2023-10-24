package com.yongle.springboot.service.impl;

import cn.hutool.log.Log;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yongle.springboot.common.Constants;
import com.yongle.springboot.common.Result;
import com.yongle.springboot.common.StatusEnum;
import com.yongle.springboot.entity.Host;
import com.yongle.springboot.entity.Port;
import com.yongle.springboot.exception.ServiceException;
import com.yongle.springboot.mapper.*;
import com.yongle.springboot.service.IHostService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yongle.springboot.utils.OpenstackUtils;
import org.openstack4j.model.compute.Server;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yongle
 * @since 2023-03-02
 */
@Service
public class HostServiceImpl extends ServiceImpl<HostMapper, Host> implements IHostService {
    private static final Log LOG = Log.get();
    @Resource
    private HostMapper hostMapper;
    @Resource
    private FlavorMapper flavorMapper;
    @Resource
    private ImgMapper imgMapper;
    @Resource
    private PortMapper portMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Override
    @Async("asyncExecutor")
    public void insertHost(Host host, Integer taskid) {
        String name =host.getName();
        String flavor=flavorMapper.selectById(host.getFlavorId()).getValue();
        String image=imgMapper.selectById(host.getImageId()).getValue();
        Port port =portMapper.selectById(host.getPortId());
        OpenstackUtils openstackUtils =new OpenstackUtils();
        Server service = openstackUtils.createservice(name, flavor, image,port.getPort());
        host.setIpaddr(port.getIpaddr());
        host.setSubnetId(port.getSubnetId());
        port.setStatus("Active");
        portMapper.updateById(port);
        host.setHost(service.getId());
        host.setStatus(openstackUtils.bulid(host.getHost()));
        host.setUrl(openstackUtils.geturl(host.getHost()));
        hostMapper.updateById(host);
        stringRedisTemplate.opsForValue().set(taskid.toString(),StatusEnum.FINISHED.toString());
    }

    @Override
    public void delHost(Integer id) {
        if(hostMapper.selectById(id).getPortId()!=null){
            Port port =portMapper.selectById(hostMapper.selectById(id).getPortId());
            port.setStatus("DOWN");
            portMapper.updateById(port);
        }
        if(hostMapper.selectById(id).getHost()!=null){
            OpenstackUtils openstackUtils = new OpenstackUtils();
            try{
                openstackUtils.delhost(hostMapper.selectById(id).getHost());
            }catch (NullPointerException e){
                LOG.error(e);
                throw new ServiceException(Constants.CODE_500, "主机创建中");
            }

        }
       hostMapper.deleteById(id);
    }

    @Override
    public Page<Host> findpage(Page<Host> page, String name, String userName, String rangceName, String no, String subnetName, String ipaddr) {
        return hostMapper.findpage(page,name,userName,rangceName,no,subnetName,ipaddr);
    }

    @Override
    @Async("asyncExecutor")
    public void stopHost(Integer taskid, Integer id) {
        OpenstackUtils openstackUtils =new OpenstackUtils();
        Host host=hostMapper.selectById(id);
        host.setStatus(openstackUtils.shutoff(host.getHost()));
        hostMapper.updateById(host);
        stringRedisTemplate.opsForValue().set(taskid.toString(),StatusEnum.FINISHED.toString());
    }

    @Override
    @Async("asyncExecutor")
    public void startHost(Integer taskid, Integer id) {
        OpenstackUtils openstackUtils =new OpenstackUtils();
        Host host=hostMapper.selectById(id);
        host.setStatus(openstackUtils.start(host.getHost()));
        hostMapper.updateById(host);
        stringRedisTemplate.opsForValue().set(taskid.toString(),StatusEnum.FINISHED.toString());
    }

    @Override
    public Result geturl(Integer id) {
        Host  host=hostMapper.selectById(id);
        OpenstackUtils openstackUtils =new OpenstackUtils();
        host.setUrl(openstackUtils.geturl(host.getHost()));
        hostMapper.updateById(host);
        return Result.success(host.getUrl());
    }

    @Override
    public Object findone(Integer id) {

        return hostMapper.findone(id);
    }


}
