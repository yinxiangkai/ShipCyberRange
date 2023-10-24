<template>
  <div>
    <div style="margin: 10px 0">
      <el-row :gutter="10">
        <el-col :span="6" v-for="item in rangces" :key="item.id" style="margin-bottom: 10px">
          <div style="border: 1px solid #ccc; padding-bottom: 10px">
            <img :src="item.url" alt="" style="width: 100%">
            <div style="color: #666; padding: 10px">靶场名称:{{ item.name }}</div>
            <div style="padding: 10px"><el-button type="primary" @click="goRangce(item.id)">查看详情</el-button></div>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
export default {
  name: "RangceList",
  data() {
    return {
      rangces: []
    }
  },
  created() {
    this.request.get("/rangce/findAll").then(res => {
      this.rangces = res.data
    })
  },
  methods: {
    goRangce(id) {
      this.$router.push({
        path: '/Uhome/rangce',
        query: {
          id: id,
        }
      })
    },
  }
}
</script>

<style scoped>

</style>