<template>
  <div class="rangce">
    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="请输入靶场名称" suffix-icon="el-icon-search" v-model="rangceName"></el-input>
      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
    </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'" >
      <el-table-column prop="id" label="ID" width="80" sortable></el-table-column>
      <el-table-column prop="rangceName" label="靶场名" width="180"></el-table-column>
      <el-table-column prop="grade" label="靶场总分" width="80"></el-table-column>
      <el-table-column prop="myGrade" label="已获得分数" width="100"></el-table-column>
      <el-table-column prop="status" label="状态">
        <template v-slot="scope">
          <el-tag size="medium"  type="info" v-if="scope.row.status==='Build'">创建中</el-tag>
          <el-tag size="medium"  type="success" v-if="scope.row.status==='Active'">使用中</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="applyTime" label="申请时间" width="150"></el-table-column>
      <el-table-column prop="endTime" label="结束时间" width="150"></el-table-column>
      <el-table-column label="操作"  width="248"  align="center">
        <template v-slot="scope">
          <el-button type="success" @click="goRangce(scope.row.rangceId)">查看详情</el-button>
          <el-button type="primary" @click="abandon(scope.row)">释放靶场</el-button>
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
  name: "MyRangce",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      form: {},
      rangceName:"",
      user:Cookies.get('cookie')? JSON.parse(Cookies.get('cookie')):{},
    }
  },
  created() {
    this.load()
    this.timeGet()
  },
  methods: {

    load() {
      this.request.get("/rangce-group/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          userId: this.user.uid,
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
          this.$msgbox(res.msg)
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    goRangce(id) {
      this.$router.push({
        path: '/Uhome/rangce',
        query: {
          id: id,
        }
      })
    },
    timeGet(){
      setInterval(this.getStatus, 20000); // 注意: 第一个参数为方法名的时候不要加括号;
    },
    getStatus(){
      this.$store.state.task.forEach(element=>{
        if(element.status==="UNFINISHED"){
          this.request.get("/task/status/"+element.id).then(res => {
            element.status=res.data
            if(element.status==="FINISHED"){
              this.load()
              this.$message.success(element.id+"号任务完成")
            }
          })
        }
      })

    },


  }
}
</script>

<style scoped>
.rangce{
  position: relative;
  width: 90%;
  margin: 0 auto;
}
</style>