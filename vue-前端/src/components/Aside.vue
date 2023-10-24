<template>
  <el-menu default-active="/Ahome/Dashboard"
      :default-openeds="['1', '1']" style="min-height: 100%; overflow-x: hidden"
           background-color="rgb(48, 65, 86)"
           text-color="#fff"
           active-text-color="#ffd04b"
           :collapse-transition="true"
           :collapse="isCollapse"
           router
  >
    <div style="height: 60px; line-height: 60px; text-align: center">
      <img src="../assets/logo.jpg" alt="" style="width: 20px; position: relative; top: 5px; right: 5px">
      <b style="color: white" v-show="logoTextShow">靶场后台管理</b>
    </div>
    <el-menu-item index="/Ahome/Dashboard">
      <i class="iconfont icon-evaluation">&nbsp &nbsp</i>
      <span slot="title">数据面板</span>
    </el-menu-item>
    <el-menu-item index="/Ahome/user_manager">
      <i class="iconfont icon-yonghu">&nbsp &nbsp</i>
      <span slot="title">用户管理</span>
    </el-menu-item>
    <el-submenu index="2">
      <template slot="title">
        <i class="iconfont icon-ziyuan">&nbsp &nbsp</i>
        <span>资源管理</span>
      </template>
      <el-menu-item index="/Ahome/img_manager">
        <i class="iconfont icon-jingxiangguanli">&nbsp &nbsp</i>
        <span slot="title">镜像管理</span>
      </el-menu-item>
      <el-menu-item index="/Ahome/rangce_manager">
        <i class="iconfont icon-targetbazi">&nbsp &nbsp</i>
        <span slot="title">靶场管理</span>
      </el-menu-item>
      <el-menu-item index="/Ahome/flavor">
        <i class="iconfont icon-fuwuleixing">&nbsp &nbsp</i>
        <span slot="title">规格管理</span>
      </el-menu-item>
      <el-menu-item index="/Ahome/network">
        <i class="iconfont icon-wangluo" >&nbsp &nbsp</i>
        <span slot="title">网络管理</span>
      </el-menu-item>
      <el-menu-item index="/Ahome/host_manager">
        <i class="iconfont icon-server-host-full" >&nbsp &nbsp</i>
        <span slot="title">主机管理</span>
      </el-menu-item>
    </el-submenu>
    <el-submenu index="4">
      <template slot="title">
        <i class="iconfont icon-xiaoxizhongxin">&nbsp &nbsp</i>
        <span slot="title">消息通知</span>
      </template>
      <el-menu-item index="/Ahome/webchat">
        <i class="iconfont icon-xiaoxi1" >&nbsp &nbsp</i>
        <span slot="title">在线通讯</span>
      </el-menu-item>
      <el-menu-item index="/Ahome/notice">
        <i class="iconfont icon-gonggao1" >&nbsp &nbsp</i>
        <span slot="title">公告通知</span>
      </el-menu-item>
      <el-menu-item index="/Ahome/cover">
        <i class="iconfont icon-wenanfengmian" >&nbsp &nbsp</i>
        <span slot="title">轮播图管理</span>
      </el-menu-item>
    </el-submenu>
  </el-menu>
</template>

<script>
export default {
  name: "Aside",
  props: {
    isCollapse: Boolean,
    logoTextShow: Boolean
  },
}
</script>

<style scoped>


</style>