package com.yongle.springboot.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yongle.springboot.common.Result;
import com.yongle.springboot.entity.Network;
import com.yongle.springboot.entity.Vis;
import com.yongle.springboot.entity.dto.Edge;
import com.yongle.springboot.entity.dto.Node;
import com.yongle.springboot.entity.dto.VisData;
import com.yongle.springboot.mapper.SubnetMapper;
import com.yongle.springboot.utils.OpenstackUtils;
import com.yongle.springboot.entity.RangceHost;
import com.yongle.springboot.mapper.RangceHostMapper;
import com.yongle.springboot.service.IRangceHostService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yongle
 * @since 2023-04-16
 */
@Service
public class RangceHostServiceImpl extends ServiceImpl<RangceHostMapper, RangceHost> implements IRangceHostService {
        @Resource
        private RangceHostMapper rangceHostMapper;
        @Resource
        private SubnetMapper subnetMapper;

    @Override
    public VisData getvisMap(String rangceId, Integer userId) {

        VisData visData=new VisData();
        List<Node> nodes= new LinkedList<>();
        List<Edge>edges= new LinkedList<>();
        List<RangceHost> rangceHostList = rangceHostMapper.selectRangceHostList(rangceId,userId);
        for (RangceHost rangceHost:rangceHostList){
            Node node = new Node();
            Edge edge = new Edge();
            node.setId(rangceHost.getNumber());
            node.setImage(rangceHost.getImageUrl());
            node.setIpaddr(rangceHost.getIpaddr());
            nodes.add(node);
            if(rangceHost.getNumber()!=0){
                edge.setTo(rangceHost.getNumber());
                edge.setFrom(0);
                edges.add(edge);
            }

        }
        visData.setNodes(nodes);
        visData.setEdges(edges);
        return visData;
    }

    @Override
    public Result getDownList(String rangceId, Integer userId) {
        List<RangceHost> downList = rangceHostMapper.selectDownList(rangceId,userId);
        QueryWrapper<RangceHost> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("rangce_id", rangceId);
        queryWrapper.eq("type", "1");
        List<RangceHost> notDownList=rangceHostMapper.selectList(queryWrapper);
        for(int i=0;i<notDownList.size();i++)
        {
            for (RangceHost rangceHost : downList) {
                if (Objects.equals(notDownList.get(i).getId(), rangceHost.getId())) {
                    notDownList.remove(i);
                }
            }
        }
        Map<String, List<RangceHost>> map = new HashMap<>();
        map.put("downList", downList);
        map.put("notDownList", notDownList);
        return Result.success(map);
    }
}
