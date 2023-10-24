<template>
  <div>
    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="请输入靶场名称" suffix-icon="el-icon-search" v-model="rangceName"></el-input>
      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
    </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'" >
      <el-table-column prop="id" label="ID" width="80" sortable></el-table-column>
      <el-table-column prop="rangceName" label="靶场靶场" width="180"></el-table-column>
      <el-table-column prop="no" label="主机组编号" width="180"></el-table-column>
      <el-table-column prop="userName" label="所有者" width="180"></el-table-column>
      <el-table-column prop="applyTime" label="申请时间" width="180"></el-table-column>
      <el-table-column label="操作"  width="280"  align="center">
        <template v-slot="scope">
          <el-button type="primary" @click="abandon(scope.row)">用户释放</el-button>
        </template>
      </el-table-column>
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
  name: "UserRangce",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      form: {},
      userId:this.$route.query.id,
      rangceName:"",
    }
  },
  created() {
    this.load()
  },
  methods: {

    load() {
      this.request.get("/rangce-group/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          userId: this.userId,
          rangceName:this. rangceName
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
    abandon(row) {
      this.request.get("/rangce-group/abandon",  {
        params: {

          uid:row.userId,
          rangceId:row.rangceId,
        }
      }).then(res => {
        if (res.code === '200') {
          this.role=res.data
          this.load()
          this.$message.success(res.msg)
        } else {
          this.$message.error(res.msg)
        }
      })
    },


  }
}
</script>

<style scoped>

</style>