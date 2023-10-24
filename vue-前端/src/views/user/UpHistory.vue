<template>
  <div class="history">
    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="请输入靶场名称" suffix-icon="el-icon-search" v-model="rangceName"></el-input>
      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
    </div>

    <el-table :data="tableData" class="table" border stripe :header-cell-class-name="'headerBg'" >
      <el-table-column prop="rangceName" label="靶场" width="180"></el-table-column>
      <el-table-column prop="grade" label="得分" width="180"></el-table-column>
      <el-table-column prop="flag" label="flag码" width="180"></el-table-column>
      <el-table-column prop="time" label="提交时间" width="180"></el-table-column>
    </el-table>
    <div style="padding: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[2, 5, 10, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>
  </div>
</template>

<script>
import Cookies from "js-cookie";

export default {
  name: "UpHistory",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      form: {},
      userId:this.$route.query.id,
      rangceName:"",
      user:Cookies.get('cookie')? JSON.parse(Cookies.get('cookie')):{},
    }
  },
  created() {
    this.load()
  },
  methods: {

    load() {
      this.request.get("/flag/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          rangceName:this. rangceName,
          userId: this.user.uid,
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    del(id) {
      this.request.delete("/rangce-group/del/" + id).then(res => {
        if (res.code === '200') {
          this.$message.success(res.msg)
          this.load()
        } else {
          this.$message.error(res.msg)
        }
      })
    },

    reset() {
      this.no= ""
      this.load()
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.load()
    },
    handleAdd() {
      this.dialogFormVisible = true
    },


  }
}
</script>

<style scoped lang="less">
.history{
  position: relative;
  width: 60.2%;
  margin: 0 auto;

}
</style>