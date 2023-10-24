<template>
  <div>
    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="no"></el-input>
      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
      <el-button type="primary" @click="handleAdd">新增 <i class="el-icon-circle-plus-outline"></i></el-button>
    </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'" >
      <el-table-column prop="id" label="ID" width="80" sortable></el-table-column>
      <el-table-column prop="no" label="主机组编号" width="180"></el-table-column>
      <el-table-column prop="rangceName" label="所属靶场" width="180"></el-table-column>
      <el-table-column prop="userName" label="使用用户" width="180"></el-table-column>
      <el-table-column prop="applyTime" label="申请时间" width="180"></el-table-column>
      <el-table-column prop="endTime" label="结束时间" width="180"></el-table-column>
      <el-table-column label="操作"  width="280"  align="center">
        <template v-slot="scope">
          <el-button type="success" @click="handleEdit(scope.row)">主机管理<i class="el-icon-edit"></i></el-button>
          <el-button type="primary" @click="abandon(scope.row)">用户释放</el-button>
          <el-popconfirm
              class="ml-5"
              confirm-button-text='确定'
              cancel-button-text='我再想想'
              icon="el-icon-info"
              icon-color="red"
              title="您确定删除吗？"
              @confirm="del(scope.row.id)"
          >
            <el-button type="danger" slot="reference">删除 <i class="el-icon-remove-outline"></i></el-button>
          </el-popconfirm>
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
    <el-dialog title="添加主机组" :visible.sync="dialogFormVisible" width="30%" :close-on-click-modal="false">
      <el-form label-width="90px" size="small" style="width: 90%">
        <el-form-item label="主机组编号">
          <el-input v-model="form.no" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import Cookies from "js-cookie";

export default {
  name: "RangceGroup",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      form: {},
      no:"",
      rangce_id:this.$route.query.id,
      dialogFormVisible: false,
      dialogFormVisible1: false,
      fullscreenLoading: false,
      multipleSelection: [],
      user: Cookies.get('cookie')? JSON.parse(Cookies.get('cookie')):{},
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
          no: this.no,
          rangce_id:this.rangce_id,
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    save() {
      this.fullscreenLoading = true;
      this.form.rangceId=this.rangce_id
      this.request.post("/rangce-group/save", this.form).then(res => {
        if (res.code === '200') {
          this.fullscreenLoading = false;
          this.$message.success(res.msg)
          this.dialogFormVisible = false
          this.load()
        } else {
          this.fullscreenLoading = false;
          this.$message.error(res.msg)
        }
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
    handleEdit(row) {
      this.$router.push({
        path: '/Ahome/rangce_host',
        query: {
          groupId:row.id,
          rangceId: this.rangce_id,
          userId:row.userId,
        }
      })
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