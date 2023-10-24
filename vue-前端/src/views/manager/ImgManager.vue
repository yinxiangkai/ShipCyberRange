<template>
<div class="image">
  <div style="margin: 10px 0">
    <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="name"></el-input>
    <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
    <el-button type="warning" @click="reset">重置</el-button>
    <el-button type="primary" style="margin-left:10px " @click="handleAdd">新增 <i class="el-icon-circle-plus-outline"></i></el-button>
  </div>
  <div class="table">
    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'" >
      <el-table-column prop="id" label="镜像编号" width="80"></el-table-column>
      <el-table-column prop="name" label="镜像名称"></el-table-column>
      <el-table-column prop="status" label="状态">
        <template v-slot="scope">
          <el-tag size="medium"  type="info" v-if="scope.row.status==='Build'">创建中</el-tag>
          <el-tag size="medium"  type="success" v-if="scope.row.status==='Active'">运行中</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="time" label="创建时间"></el-table-column>
      <el-table-column prop="nickName" label="创建者"></el-table-column>
      <el-table-column prop="discription" label="描述">
        <template v-slot="scope">
          <el-popover trigger="hover" placement="top">
           <span> {{ scope.row.discription }}</span>
            <div slot="reference" class="name-wrapper">
              <el-tag size="medium">描述</el-tag>
            </div>
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column label="操作"  width="280" align="center">
        <template v-slot="scope">
          <el-button type="success" @click="handleEdit(scope.row)">编辑 <i class="el-icon-edit"></i></el-button>
          <el-popconfirm   class="ml-5"  confirm-button-text='确定'  cancel-button-text='我再想想'
                           icon="el-icon-info"  icon-color="red"   title="您确定删除吗？"   @confirm="del(scope.row.id)"   >
            <el-button type="danger" slot="reference">删除<i class="el-icon-remove-outline"></i></el-button>
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
  <el-dialog title="镜像信息" :visible.sync="dialogFormVisible" width="30%" :close-on-click-modal="false">
    <el-form label-width="100px" size="small" style="width: 90%">
      <el-form-item label="镜像名称">
        <el-input v-model="form.name" autocomplete="off" style="width: 200px"></el-input>
      </el-form-item>
      <el-form-item label="文件选择">
        <el-upload
            class="avatar-uploader"
            action="http://localhost:8081/file/upload"
            :show-file-list="true"
            :on-success="handleAvatarSuccess"

        >
          <i  class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload>
      </el-form-item>
      <el-form-item label="描述">
        <el-input type="textarea" v-model="form.discription"></el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogFormVisible = false">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </div>
  </el-dialog>
  <el-dialog title="修改镜像信息" :visible.sync="dialogFormVisible1" width="30%" :close-on-click-modal="false">
    <el-form label-width="100px" size="small" style="width: 90%">
      <el-form-item label="镜像名称">
        <el-input v-model="form.name" autocomplete="off" style="width: 200px"></el-input>
      </el-form-item>
      <el-form-item label="描述">
        <el-input type="textarea" v-model="form.discription"></el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogFormVisible1 = false">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </div>
  </el-dialog>
</div>
</template>

<script>
import Cookies from "js-cookie";
export default {
  name: "ImgManager",
  data (){
    return {
      tableData:[],
      form: {},
      name:"",
      total: 0,
      pageNum: 1,
      pageSize: 10,
      user: Cookies.get('cookie')? JSON.parse(Cookies.get('cookie')):{},
      dialogFormVisible: false,
      dialogFormVisible1: false,


    }
  },
  created() {
  this.load()
  this.timeGet()
  },
  methods: {
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
    load() {
      this.request.get("/img/page", {
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
    handleAdd() {
      this.dialogFormVisible = true
      this.form = {}
    },
    del(id) {
      this.request.delete("/img/del/" + id).then(res => {
        if (res.code === '200') {
          this.$message.success("删除成功")
          this.load()
        } else {
          this.$message.error("删除失败")
        }
      })
    },
    save() {
      this.form.userId=this.user.uid
      this.request.post("/img/save", this.form).then(res => {
        if (res.code === '200') {
          if(res.data!==null){
            this.$store.state.task.push({
              id: res.data.id,
              status:res.data.status
            })
          }
          this.$message.success(res.msg)
          this.dialogFormVisible = false
          this.dialogFormVisible1 = false
          this.load()
        } else {
          this.$message.error("保存失败")
        }
      })
    },
    handleAvatarSuccess(res){
      this.form.url=res.data

    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible1 = true
    },
    timeGet(){
      setInterval(this.getStatus, 2000); // 注意: 第一个参数为方法名的时候不要加括号;
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

</style>

