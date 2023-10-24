package com.yongle.springboot.utils;
import cn.hutool.log.Log;
import com.yongle.springboot.common.Result;
import com.yongle.springboot.common.StatusEnum;
import org.openstack4j.api.Builders;
import org.openstack4j.api.OSClient.OSClientV3;
import org.openstack4j.api.identity.EndpointURLResolver;
import org.openstack4j.api.types.Facing;
import org.openstack4j.core.transport.Config;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.model.common.Payload;
import org.openstack4j.model.common.Payloads;
import org.openstack4j.model.compute.*;
import org.openstack4j.model.identity.URLResolverParams;
import org.openstack4j.model.image.v2.ContainerFormat;
import org.openstack4j.model.image.v2.DiskFormat;
import org.openstack4j.model.network.*;
import org.openstack4j.openstack.OSFactory;
import org.springframework.stereotype.Component;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;


@Component
public class OpenstackUtils {
    private static final Log LOG = Log.get();
    final EndpointURLResolver endpointUrlResolver = new EndpointURLResolver() {
        @Override
        public String findURLV2(URLResolverParams params) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String findURLV3(URLResolverParams params) {
            switch (params.type.getServiceName()) {
                case "keystone":
                    return "http://222.27.255.229:5000/v3";
                case "glance":
                    return "http://222.27.255.229:9293/v2";// 经openstack4j检验用的是v2版本
                case "nova":
                    return "http://222.27.255.229:8775/v2.1";
                // compute计算相关
                case "neutron":
                    return "http://222.27.255.229:9697";
                case "cinder":
                    return "http://222.27.255.229:8777/v2";
                // 经openstack4j检验用的是v2版本

                default:
                    return null;
            }
        }
//            @Override
//            public String findURLV3(URLResolverParams params) {
//                switch (params.type.getServiceName()) {
//                    case "keystone":
//                        return "http://192.168.186.100:5000/v3";
//                    case "glance":
//                        return "http://192.168.186.100:9292/v2";// 经openstack4j检验用的是v2版本
//                    case "nova":
//                        return "http://192.168.186.100:8774/v2.1/eeb0321701f2453caea33e291f3c554a";
//                    // compute计算相关
//                    case "neutron":
//                        return "http://192.168.186.100:9696";
//                    case "cinder":
//                        return "http://192.168.186.100:8776/v2/eeb0321701f2453caea33e291f3c554a";
//                    // 经openstack4j检验用的是v2版本
//
//                    default:
//                        return null;
//                }
//            }
    };

    // OSFactory.enableHttpLoggingFilter(true);
    //Identifier domainIdentifier = Identifier.byName("Default");

    // 不同域内的项目名和用户名有可能相同。
//    OSClientV3 os = OSFactory.builderV3()
//            .withConfig(Config.newConfig().withEndpointURLResolver(endpointUrlResolver))
//            .endpoint("http://192.168.186.100:5000/v3").credentials("admin", "123456", Identifier.byName("Default"))
//            .scopeToProject(Identifier.byName("admin"), Identifier.byName("default")).perspective(Facing.PUBLIC)
//            .authenticate();
    OSClientV3 os = OSFactory.builderV3()
            .withConfig(Config.newConfig().withEndpointURLResolver(endpointUrlResolver))
            .endpoint("http://222.27.255.229:5000/v3").credentials("admin", "123456",Identifier.byName("Default"))
            .scopeToProject(Identifier.byName("admin"), Identifier.byName("default")).perspective(Facing.PUBLIC)
            .authenticate();

    public Result getNetwork(){
        List<? extends Network> networks = os.networking().network().list();
        return Result.success(networks);
    }

    public Result getimage(){
        List<? extends Image> images = os.compute().images().list();

        return Result.success(images,"应该返回有cirros");
    }
    public Result getserver(){
        List<? extends Server> servers = os.compute().servers().list();

        return Result.success(servers,"应该返回有cirros");
    }

    public Result getflavors(){

        List<? extends Flavor> flavors = os.compute().flavors().list();
        return Result.success( flavors ,"是有数据的");
    }
    public Result getports(){

        List<? extends Port> ports = os.networking().port().list();
        return Result.success( ports ,"是有数据的");
    }



    //上传镜像
    public String uploadimag(String url,String name) throws MalformedURLException {

        org.openstack4j.model.image.v2.Image image = os.imagesV2().create(
                Builders.imageV2()
                        .name(name)
                        .containerFormat(ContainerFormat.BARE)
                        .visibility(org.openstack4j.model.image.v2.Image.ImageVisibility.PRIVATE)
                        .diskFormat(DiskFormat.QCOW2)
                        .minDisk(0L)
                        .minRam(0L)
                        .build()
        );
        Payload<URL> payload = Payloads.create(new URL(url));
        ActionResponse upload = os.imagesV2().upload(
                image.getId(),
                payload,
                image);
        return image.getId();
    }
    public String imgBulid(String id){

        org.openstack4j.model.image.v2.Image image=null;
        while(image==null){
            try {
               image = (org.openstack4j.model.image.v2.Image)os.imagesV2().get(id);
            } catch (Exception e) {
                LOG.error(e);
            }
        }
        while(!image.getStatus().value().equals("active"))
        {
            image = (org.openstack4j.model.image.v2.Image) os.imagesV2().get(id);
            System.out.println(image.getStatus().value());
        }
        return StatusEnum.Active.toString();
    }

    //删除镜像
    public void delimg(String id){


        os.imagesV2().delete(id);
    }

    //创建实例类型
    public String createFlavor(com.yongle.springboot.entity.Flavor myflavor){

        Flavor flavor = Builders.flavor()
                .name(myflavor.getName())
                .ram(myflavor.getRam())
                .vcpus(myflavor.getVcpus())
                .disk(myflavor.getDisk())
                .build();

        flavor = os.compute().flavors().create(flavor);

        return  flavor.getId();

    }

    //删除实例类型
    public void delflavor(String id){


        os.compute().flavors().delete(id);
    }

    //创建网络
    public Network createnetwork(String name){
                Network network = os.networking().network().create(Builders.network()
                .name(name)
                .tenantId("56fdc3aefa9c4257a25fe26b527541d1")
                .adminStateUp(true)
                .isShared(true)
                .isRouterExternal(true)
                .build());
        return  network;
    }
    public Network createNetwork(String name){
        Network network = os.networking().network().create(Builders.network()
                .tenantId("56fdc3aefa9c4257a25fe26b527541d1")
                .name(name)
                .adminStateUp(true)
                .isShared(true)
                .isRouterExternal(true)
                .build());
        return  network;
    }

    //删除网络
    public void delnetwork(String id){
        os.networking().network().delete(id);
    }
    //创建子网
    public Subnet createsubnet(com.yongle.springboot.entity.Subnet mysubnet){

        Subnet subnet = os.networking().subnet().create(Builders.subnet()
                .name(mysubnet.getName())
                .networkId(mysubnet.getNetwork())
                .tenantId("56fdc3aefa9c4257a25fe26b527541d1")
                .enableDHCP(true)
                .addDNSNameServer("8.8.8.8")
                .ipVersion(IPVersionType.V4)
                .cidr(mysubnet.getCidr())
                .build());
        return subnet;
    }
    public Subnet createSubnet(com.yongle.springboot.entity.Subnet mysubnet){

        Subnet subnet = os.networking().subnet().create(Builders.subnet()
                .name(mysubnet.getName())
                .networkId(mysubnet.getNetwork())
                .tenantId("56fdc3aefa9c4257a25fe26b527541d1")
                .enableDHCP(true)
                .addDNSNameServer("8.8.8.8")
                .ipVersion(IPVersionType.V4)
                .cidr(mysubnet.getCidr())
                .build());
        return subnet;
    }
   //删除子网
   public void delsubnet(String id){
       os.networking().subnet().delete(id);
   }



   //创建端口
    public  Port createport(String name ,String networkId,String ipaddr,String subnetId){
        Port port = os.networking().port().create(Builders.port()
                .name(name)
                .networkId(networkId)
                .fixedIp(ipaddr, subnetId)
                .build());
        return port;
    }

   //删除端口
   public void delport(String id){
       os.networking().port().delete(id);
   }
    //创建主机
    public Server createservice(String name,String flavor,String image,String port){

        ServerCreate sc = Builders.server()
                .name(name)
                .flavor(flavor)
                .image(image)
                .addNetworkPort(port)
                .keypairName("admin-key")
                .build();
        Server server = os.compute().servers().boot(sc);
        return server;
    }
    public Server createService(String name,String flavor,String image,List<String> networkList){

        ServerCreate sc = Builders.server()
                .name(name)
                .flavor(flavor)
                .image(image)
                .networks(networkList)
                .keypairName("admin-key")
                .build();
        Server server = os.compute().servers().boot(sc);
        return server;
    }

    //删除主机
    public void delhost(String id){


        os.compute().servers().delete(id);
    }
    public String bulid(String id){

        Server server=null;
        while(server==null){
            try {
                server= os.compute().servers().get(id);
            } catch (Exception e) {
                LOG.error(e);
            }
        }
        while(!server.getStatus().value().equals("active"))
        {
            server= os.compute().servers().get(id);
        }
        return StatusEnum.Active.toString();
    }
    //获取vncUrl
    public String  geturl(String id){

        VNCConsole console = os.compute().servers().getVNCConsole(id, VNCConsole.Type.NOVNC);
        return console.getURL();
    }
    //获取主机状态
    public  String getStatus(String id){


        return  os.compute().servers().get(id).getStatus().value();
    }
    //关机
    public String shutoff(String id){

        os.compute().servers().action(id, Action.STOP);
        String status=getStatus(id);
        while(!status.equals("shutoff")){
            status=getStatus(id);
        }
        return StatusEnum.Shutoff.toString();
    }
    //关机
    public String start(String id){

        os.compute().servers().action(id, Action.START);
        String status=getStatus(id);
        while(!status.equals("active")){
            status=getStatus(id);
        }
        return StatusEnum.Active.toString();
    }

}

