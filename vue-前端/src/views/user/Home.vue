<template>
  <div style="margin: 0 0">
    <el-carousel height="750px"  :interval="10000">
      <el-carousel-item v-for="(item,index) in covers" :key="index">
        <img :src="item.url" alt="" width="100%">
      </el-carousel-item>
    </el-carousel>

    <el-card style="margin: 0 auto;">
      <div style="padding-bottom: 20px; font-size: 30px ;font-family: 楷体,serif;flood-color: #7cd2ef;"> 系 统 公 告</div>
      <el-collapse accordion v-for="(item, index) in notice"  :key='index'>
        <el-collapse-item :name="index + ''" >
          <template slot="title" >
            <span style="font-size: 26px; color: #479de1;">{{ item.title }}</span>
            <i style="color: #47e63c" class="header-icon el-icon-info"></i>
            <div style="padding: 10px 0;width: 40px; margin-top: 30px;margin-left:10px;"><el-image :src="item.img"></el-image></div>
            <span style="font-size: 18px;margin-left: 20px">{{ item.time }}</span>
            <span style="font-size: 18px;margin-left: 20px">发布者：{{ item.user }}</span>
          </template>
          <div>
            <div v-html="item.content"></div>
          </div>
        </el-collapse-item>
      </el-collapse>
    </el-card>
  </div>
</template>

<script>
import Cookies from "js-cookie";

export default {
  name: "Home",
  data() {
    return {
      covers: [],
      activeNames: ['0'],
      user: Cookies.get('cookie')? JSON.parse(Cookies.get('cookie')):{},
      notice: [],
    }
  },
  created() {
    this.request.get("/cover/get").then(res => {
      this.covers = res.data.splice(0, 10)
    })
    this.request.get("/notice/get").then(res => {
      this.notice = res.data.splice(0, 10)
    })
  },
  methods: {

  }
}
</script>

<style scoped>

</style>