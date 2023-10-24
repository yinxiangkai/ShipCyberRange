<template>
  <el-container class="superadmin">
    <el-header>
      <el-menu  class="menu" >
        <el-submenu index="1" class="menu-1" >
          <template slot="title"> <span :class="usericon"></span></template>
          <el-menu-item index="2-1" @click="changepassword = true">修改密码</el-menu-item>
          <el-menu-item index="2-2" @click="logout">退出</el-menu-item>
        </el-submenu>

      </el-menu>

    </el-header>
    <el-main>


      <el-tabs type="card">
        <el-tab-pane label="用户管理">   <div style="margin: 10px 0">
          <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="username"></el-input>
          <el-button class="ml-5" type="primary" @click="search">搜索</el-button>
          <el-button type="warning" @click="reset">重置</el-button>
        </div>

          <div style="margin: 10px 0">
            <el-button type="primary" @click="handleAdd">新增 <i class="el-icon-circle-plus-outline"></i></el-button>
            <el-popconfirm
                class="ml-5"
                confirm-button-text='确定'
                cancel-button-text='取消'
                icon="el-icon-info"
                icon-color="red"
                title="您确定批量删除这些数据吗？"
                @confirm="delBatch"
            >
              <el-button type="danger" slot="reference">批量删除 <i class="el-icon-remove-outline"></i></el-button>
            </el-popconfirm>
          </div>

          <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"  @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55"></el-table-column>
            <el-table-column prop="uid" label="ID" width="80"></el-table-column>
            <el-table-column prop="username" label="账号"></el-table-column>
            <el-table-column prop="nickname" label="昵称"></el-table-column>
            <el-table-column prop="email" label="邮箱"></el-table-column>
            <el-table-column prop="createtime" label="创建时间"></el-table-column>
            <el-table-column label="操作"  width="280" align="center">
              <template slot-scope="scope">
                <el-popconfirm
                    class="ml-5"
                    confirm-button-text='确定'
                    cancel-button-text='我再想想'
                    icon="el-icon-info"
                    icon-color="red"
                    title="您确定删除吗？"
                    @confirm="del(scope.row.uid)"
                >
                  <el-button type="danger" slot="reference">删除 <i class="el-icon-remove-outline"></i></el-button>
                </el-popconfirm>
              </template>
            </el-table-column>
          </el-table>
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
        </el-tab-pane>
        <el-tab-pane label="系统资源">
          <el-form :model="resource" style="width: 50%;margin-left: 25%" >
            <el-form-item label="cpu数量">
              <el-input  v-model="resource.cpu"  type="number" autocomplete="off"  ></el-input>
            </el-form-item>
            <el-form-item label="内存大小 MB">
              <el-input v-model="resource.ram"  type="number" autocomplete="off"  ></el-input>
            </el-form-item>
            <el-form-item label="磁盘大小 GB">
              <el-input   v-model="resource.disk"  type="number" autocomplete="off"  ></el-input>
            </el-form-item>
            <el-button type="primary" @click="save" style="margin-left: 80%">修 改</el-button>
          </el-form>


        </el-tab-pane>

      </el-tabs>

    </el-main>
    <el-dialog title="修改密码" :visible.sync="changepassword" width="30%" :modal="true" :modal-append-to-body='false'>
      <el-form :model="user" :rules="rules" ref="userForm" >
        <el-form-item prop="username">
          <el-input  placeholder="请输入账号"  v-model="user.username"  clearable></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input placeholder="请输入新密码" v-model="user.password" show-password></el-input>
        </el-form-item>
        <el-form-item prop="newpassword">
          <el-input placeholder="请输入新密码" v-model="user.newpassword" show-password></el-input>
        </el-form-item>
        <el-form-item prop="repassword">
          <el-input placeholder="请确认新密码" v-model="user.repassword" show-password></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="changepassword = false">取 消</el-button>
        <el-button type="primary" @click="changepasswd">修改密码</el-button>
      </div>
    </el-dialog>
    <el-dialog title="用户信息" :visible.sync="adduserdialog" width="30%" :modal="true" :modal-append-to-body='false' >
      <el-form label-width="80px" size="small">
        <el-form-item label="用户名">
          <el-input v-model="form.username" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="adduserdialog = false">取 消</el-button>
        <el-button type="primary" @click="add">确 定</el-button>
      </div>
    </el-dialog>

  </el-container>


</template>

<script>

export default {
  name: "superadmin",
  data (){
    return {
      tableData: [ ],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      username: "",
      collapseBtnClass: 'el-icon-s-fold',
      isCollapse: false,
      sideWidth: 200,
      logoTextShow: true,
      headerBg: 'headerBg',
      form: {},
      resource:{},
      user:{
        username:"admin",
        password:"",
        newpassword:"",
        repassword:""
      },
      rules:{
       password: [
          {required: true, message: '未输入密码', trigger: 'blur'},
          {min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur'},
        ],
        newpassword: [
          {required: true, message: '未输入新密码', trigger: 'blur'},
          {min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur'},
        ],
        repassword:[
          {required: true, message: '未确认新密码', trigger: 'blur'},

        ]

      },
      changepassword: false,
      adduserdialog: false,
      usericon:'el-icon-user',

    }
  },
  created() {
    this.load()
    this.load1()
  },
  methods:{
    save() {
      this.resource.id=1
      this.request.post("/dashboard/save", this.resource).then(res => {
        if (res.code === '200') {
          this.$message.success(res.msg)
          this.load1()
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    changepasswd(){
      this.$refs['userForm'].validate((valid) => {
        if (valid) {  // 表单校验合法
          if(this.user.newpassword !==this.user.repassword)
          {
            this.$message.error("两次密码不一致")
            return false;
          }
          console.log(this.user)
          this.request.post("/user/changepassword",this.user).then(res =>{
            if(res.code === "200") {
              this.$message.success(res.msg)
              this.user.newpassword=null
              this.user.password=null
              this.user.repassword=null
              this.changepassword=false
            } else {
              this.$message.error(res.msg)
            }
          })
        } else {
          return false;
        }
      });
    },
    load() {
      this.request.get("/user/page", {
        params: {
          pagenumber: this.pageNum,
          pagesize: this.pageSize,
        }
      }).then(res => {
        this.total= res.data.total
        this.tableData=res.data.records
      })

    },
    load1() {
      this.request.get("/dashboard/getOne"
      ).then(res => {
        this.resource=res.data
      })

    },
    search() {
      this.request.get("/user/page/name", {
        params: {
          pagenumber: this.pageNum,
          pagesize: this.pageSize,
          username: this.username,
        }
      }).then(res => {
        console.log(res.data.records)
        console.log(res.data.total)
        this.total= res.data.total
        this.tableData=res.data.records
      })

    },
    reset() {
      this.username = ""
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
    add() {
      this.request.post("/user/addadmin", this.form).then(res => {
        if (res.code==="200") {
          this.$message.success(res.msg)
          this.adduserdialog = false
          this.load()
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    handleAdd() {
      this.adduserdialog = true
      this.form = {}
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
      console.log(val)
      this.multipleSelection = val
    },
    delBatch() {
      let uids = this.multipleSelection.map(v => v.uid)
      this.request.post("/user/del/batch",uids).then(res => {
        if (res) {
          this.$message.success("批量删除成功")
          this.load()
        } else {
          this.$message.error("批量删除失败")
        }
      })
    },
    logout() {

      this.$store.commit("logout")
      this.$message.success("退出成功")
    }
  }
}
</script>

<style scoped  lang="less">

.superadmin{

  height: 100%;
  width: 100%;
  position: fixed;
  background-color: #7cd2ef;
  .menu{
    height: 60px;
    position: absolute;
    left: 0;
    background-color: #7CD2EFFF;
    width: 100%;
    .menu-1{
      height: 60px;
      padding: 0;
      font-size: 60px;
      position: absolute;
      right: 10%;
    }
  }
  .table{
    position: absolute;
    left: 20%;
    width: 50%;

  }
}
</style>