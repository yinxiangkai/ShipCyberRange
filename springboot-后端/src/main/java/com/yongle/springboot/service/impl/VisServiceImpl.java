package com.yongle.springboot.service.impl;
import com.yongle.springboot.entity.Host;
import com.yongle.springboot.entity.Subnet;
import com.yongle.springboot.entity.dto.Edge;
import com.yongle.springboot.entity.dto.Node;
import com.yongle.springboot.entity.dto.VisData;
import com.yongle.springboot.mapper.HostMapper;
import com.yongle.springboot.mapper.SubnetMapper;
import com.yongle.springboot.utils.OpenstackUtils;
import com.yongle.springboot.entity.Vis;
import com.yongle.springboot.mapper.VisMapper;
import com.yongle.springboot.service.IVisService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.expression.spel.ast.NullLiteral;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yongle
 * @since 2023-03-26
 */
@Service
public class VisServiceImpl extends ServiceImpl<VisMapper, Vis> implements IVisService {
        @Resource
        private VisMapper visMapper;
        @Resource
        private SubnetMapper subnetMapper;

    @Override
    public VisData getVisMap(String rangceId, Integer userId) {
        VisData visData=new VisData();
        List<Node>nodes= new LinkedList<>();
        List<Edge>edges= new LinkedList<>();
        List<Vis> visList = visMapper.selectVisList(rangceId,userId);
        visList.get(0).setIpaddr(subnetMapper.getOne(rangceId,userId).getGateway());
        for (Vis vis:visList){
            Node node = new Node();
            Edge edge = new Edge();
            node.setId(vis.getNumber());
            node.setImage(vis.getImageUrl());
            node.setIpaddr(vis.getIpaddr());
            nodes.add(node);
            if(vis.getNumber()!=0){
                edge.setTo(vis.getNumber());
                edge.setFrom(0);
                edges.add(edge);
            }

        }
        visData.setNodes(nodes);
        visData.setEdges(edges);
        return visData;
    }
}
