<template>
  <el-container style="min-height: 100vh">

    <el-aside :width="sideWidth + 'px'" style="box-shadow: 2px 0 6px rgb(0 21 41 / 35%);">
      <Aside :isCollapse="isCollapse" :logoTextShow="logoTextShow" />
    </el-aside>

    <el-container>
      <el-header style="border-bottom: 1px solid #ccc;">
        <Header @pcollapse="collapse" :collapseBtnClass="collapseBtnClass" :collapse="isCollapse" > </Header>
      </el-header>

      <el-main style="padding: 10px ;">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>


<script>


import Aside from "@/components/Aside.vue";
import Header from "@/components/Header.vue";

export default {
name: "Ahome",
  data() {
    return {
      collapseBtnClass: 'el-icon-s-fold',
      isCollapse: false,
      sideWidth: 200,
      logoTextShow: true,
    }
  },
  components: {
    Aside,
    Header
  },
  methods: {
    collapse() {  // 点击收缩按钮触发
      this.isCollapse = !this.isCollapse
      if (this.isCollapse) {  // 收缩
        this.sideWidth = 64
        this.collapseBtnClass = 'el-icon-s-unfold'
        this.logoTextShow = false
      } else {   // 展开
        this.sideWidth = 200
        this.collapseBtnClass = 'el-icon-s-fold'
        this.logoTextShow = true
      }
    }
  }

}
</script>

<style scoped>

</style>