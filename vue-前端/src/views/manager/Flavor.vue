<template>
  <div>
    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="name"></el-input>
      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
      <el-button type="primary" @click="handleAdd">新增 <i class="el-icon-circle-plus-outline"></i></el-button>
    </div>


    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'" >
      <el-table-column prop="id" label="ID" width="80" sortable></el-table-column>
      <el-table-column prop="name" label="类型名称"></el-table-column>
      <el-table-column prop="ram" label="内存MB"></el-table-column>
      <el-table-column prop="vcpus" label="CPU个数"></el-table-column>
      <el-table-column prop="disk" label="硬盘大小GB"></el-table-column>
          <el-table-column label="操作"  width="180" align="center">
        <template v-slot="scope">
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
    <el-dialog title="信息" :visible.sync="dialogFormVisible" width="30%" :close-on-click-modal="false">
      <el-form :model="form" :rules="rules" ref="formForm" label-width="100px" size="small" style="width: 90%" >
        <el-form-item label="实例类型名称">
          <el-input v-model="form.name" autocomplete="off" style="width: 200px"></el-input>
        </el-form-item>
        <el-form-item label="内存/MB"  prop="ram">
          <el-input type="number" v-model="form.ram" autocomplete="off" style="width: 200px"></el-input>
        </el-form-item>
        <el-form-item label="cpu数量"   prop="vcpus">
          <el-input type="number" v-model="form.vcpus" autocomplete="off" style="width: 200px"></el-input>
        </el-form-item>
        <el-form-item label="根磁盘/GB"   prop="disk">
          <el-input type="number" v-model="form.disk" autocomplete="off" style="width: 200px"></el-input>
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
  name: "Flavor",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      form: {},
      name:"",
      dialogFormVisible: false,
      multipleSelection: [],
      user: Cookies.get('cookie')? JSON.parse(Cookies.get('cookie')):{},
      rules: {
        ram: [
          { required: true, message: '内存大小不能为空'},
        ],
       vcpus: [
          { required: true, message: 'cpu数量不能为空'},
        ],
        disk: [
          { required: true, message: '磁盘大小不能为空'},
        ],

      },

    }
  },
  created() {
    this.load()
  },
  methods: {

    load() {
      this.request.get("/flavor/page", {
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
    save() {
      this.$refs['formForm'].validate((valid) => {
        if (valid) {  // 表单校验合法
          this.request.post("/flavor/save", this.form).then(res => {
            if (res.code === '200') {
              this.$message.success(res.msg)
              this.dialogFormVisible = false
              this.load()
            } else {
              this.$message.error(res.msg)
            }
          })
        } else {
          return false;
        }
      });
    },


    del(id) {
      this.request.delete("/flavor/del/" + id).then(res => {
        if (res.code === '200') {
          this.$message.success(res.msg)
          this.load()
        } else {
          this.$message.error("删除失败")
        }
      })
    },

    reset() {
      this.name = ""
      this.load()
    },
    handleSizeChange(pageSize) {
      console.log(pageSize)
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      console.log(pageNum)
      this.pageNum = pageNum
      this.load()
    },
    handleAdd() {
      this.dialogFormVisible = true
    },

  }
}
</script>

<style scoped>

</style>