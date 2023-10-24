<template>
<div>
  <el-card>
    <span style="font-size: 20px"><b>主机概况</b></span>
    <div style="margin-left: 100px;margin-top: 20px">
      <div><label>主机名:</label> {{host.name}}</div>
      <div><label>主机状态:</label> {{host.status}}</div>
      <div><label>创建时间:</label> {{host.createtime}}</div>
      <div><label>所属用户:</label> {{host.userName}}</div>
      <div><label>所属靶场:</label> {{host.rangceName}}</div>
      <div><label>所属靶机组:</label> {{host.no}}</div>
      <div><label>组内编号:</label> {{host.number}}</div>
    </div>
  </el-card>
  <el-card>
    <span style="font-size: 20px"><b>网络详情</b></span>
    <div style="margin-left: 100px;margin-top: 20px">
      <div><label>端口名:</label> {{host.port.name}}</div>
      <div><label>所属网络:</label> {{host.port.network}}</div>
      <div><label>所属子网:</label> {{host.port.subnet}}</div>
      <div><label>IP地址:</label> {{host.port.ipaddr}}</div>
    </div>
  </el-card>
  <el-card>
    <span style="font-size: 20px"><b>镜像详情</b></span>
    <div style="margin-left: 100px;margin-top: 20px">
      <div><label>镜像名:</label> {{host.img.name}}</div>
      <div><label>镜像描述:</label> {{host.img.discription}}</div>
    </div>
  </el-card>
  <el-card>
    <span style="font-size: 20px"><b>规格详情</b></span>
    <div style="margin-left: 100px;margin-top: 20px">
      <div><label>规格名:</label> {{host.flavor.name}}</div>
      <div><label>内存大小:</label> {{host.flavor.ram}}</div>
      <div><label>CPU数量:</label> {{host.flavor.vcpus}}</div>
      <div><label>磁盘大小:</label> {{host.flavor.disk}}</div>
    </div>
  </el-card>
</div>
</template>

<script>
export default {
  name: "HostDetail",
  data(){
    return{
      host:{
        port:{},
        flavor:{},
        img:{}
      },
      id:this.$route.query.id
    }
  },
  created() {
    this.load()
  },
  methods: {

    load() {
      this.request.get("/host/detail/" + this.id).then(res => {
        this.host = res.data
      })
    }
  }
}
</script>

<style scoped>

</style>