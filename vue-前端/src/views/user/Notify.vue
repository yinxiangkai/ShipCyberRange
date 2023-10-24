<template>
  <div style="color: #666;font-size: 16px;">
    <el-card style="margin: 10px auto;">
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
  name: "Notify",
  data() {
    return {
      activeNames: ['0'],
      user: Cookies.get('cookie')? JSON.parse(Cookies.get('cookie')):{},
      notice: [],
    }
  },
  created() {
    this.request.get("/notice/get").then(res => {
      this.notice = res.data.splice(0, 10)
    })
  }
}
</script>
