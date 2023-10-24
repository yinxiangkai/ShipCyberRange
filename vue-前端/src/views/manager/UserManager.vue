<template>
<div class="usermanager">
  <div style="margin: 10px 0">
    <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="name"></el-input>
    <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
    <el-button type="warning" @click="reset">重置</el-button>
    <el-popconfirm
        class="ml-5"
        confirm-button-text='确定'
        cancel-button-text='取消'
        icon="el-icon-info"
        icon-color="red"
        title="您确定批量注销这些用户吗？"
        @confirm="delBatch"
    >
      <el-button type="danger" slot="reference">批量注销 <i class="el-icon-remove-outline"></i></el-button>
    </el-popconfirm>
  </div>
  <div class="table">
    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"  @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="uid" label="ID" width="80"></el-table-column>
      <el-table-column prop="username" label="账号"></el-table-column>
      <el-table-column prop="nickname" label="昵称"></el-table-column>
      <el-table-column prop="email" label="邮箱"></el-table-column>
      <el-table-column prop="createtime" label="创建时间"></el-table-column>
      <el-table-column label="操作"  width="280" align="center">
        <template v-slot="scope">
          <el-button type="success" @click="goUserRangce(scope.row.uid)">查看用户靶场</el-button>
          <el-popconfirm   class="ml-5"  confirm-button-text='确定'  cancel-button-text='我再想想'
              icon="el-icon-info"  icon-color="red"   title="您确定注销该用户吗？"   @confirm="del(scope.row.uid)"   >
            <el-button type="danger" slot="reference">注销<i class="el-icon-remove-outline"></i></el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
  </div>
  <div class="block">
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
export default {
  name: "UserManager",
  data (){
    return {
      dialogVisible: false,
      name:"",
      total: 0,
      pageNum: 1,
      pageSize: 10,
      tableData: [],

    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      this.request.get("/user/pageuser", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    del(id) {
      this.request.delete("/user/del/"+id).then(res => {
        if (res.code==="200") {
          this.$message.success(res.msg)
          this.load()
        } else {
          this.$message.error("删除失败")
        }
      })
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    reset() {
      this.name = ""
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
    delBatch() {
      if (!this.multipleSelection.length) {
        this.$message.error("请选择需要删除的数据")
        return
      }
      let ids = this.multipleSelection.map(v => v.id)  // [{}, {}, {}] => [1,2,3]
      this.request.post("/user/del/batch", ids).then(res => {
        if (res.code === '200') {
          this.$message.success("批量删除成功")
          this.load()
        } else {
          this.$message.error("批量删除失败")
        }
      })
    },
    goUserRangce(id){
      this.$router.push({
        path: '/Ahome/user_rangce',
        query: {
          id: id,
        }
      })
    }

  }
}
</script>

<style scoped lang="less">

</style>