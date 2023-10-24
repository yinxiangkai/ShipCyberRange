<<template>
  <div style="line-height: 60px; display: flex">
    <div style="flex: 1;">
      <span :class="collapseBtnClass" style="cursor: pointer; font-size: 18px" @click="collapse"></span>

    <el-breadcrumb separator="/" style="display: inline-block; margin-left: 10px">
      <el-breadcrumb-item>{{ currentPathName }}</el-breadcrumb-item>
    </el-breadcrumb>
  </div>
  <el-dropdown style="width: 150px; cursor: pointer; text-align: right">
    <div style="display: inline-block">
      <img v-if="user.avatarurl" :src="user.avatarurl" alt=""
           style="width: 30px; border-radius: 50%; position: relative; top: 10px; right: 5px">
      <span>{{user.nickname}}</span><i class="el-icon-arrow-down" style="margin-left: 5px"></i>
    </div>
    <el-dropdown-menu slot="dropdown" style="width: 100px; text-align: center">
      <el-dropdown-item style="font-size: 14px; padding: 5px 0" >
        <span @click="changepassword = true">修改密码</span>
      </el-dropdown-item>
      <el-dropdown-item style="font-size: 14px; padding: 5px 0">
        <span @click="changeinformation = true">修改个人信息</span>
      </el-dropdown-item>
      <el-dropdown-item style="font-size: 14px; padding: 5px 0">
        <span style="text-decoration: none" @click="logout">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp退出&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp</span>
      </el-dropdown-item>
    </el-dropdown-menu>
  </el-dropdown>

    <el-dialog title="修改密码" :visible.sync="changepassword" width="30%" :modal="true" :modal-append-to-body='false'>
      <el-form :model="userpassword" :rules="rules" ref="userpasswordForm" >
        <el-form-item prop="username">
          <el-input  placeholder="请输入账号"  v-model="userpassword.username"  clearable></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input placeholder="请输入新密码" v-model="userpassword.password" show-password></el-input>
        </el-form-item>
        <el-form-item prop="newpassword">
          <el-input placeholder="请输入新密码" v-model="userpassword.newpassword" show-password></el-input>
        </el-form-item>
        <el-form-item prop="repassword">
          <el-input placeholder="请确认新密码" v-model="userpassword.repassword" show-password></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="changepassword = false">取 消</el-button>
        <el-button type="primary" @click="changepasswd">修改密码</el-button>
      </div>
    </el-dialog>

    <el-dialog title="修改个人信息" :visible.sync="changeinformation" width="30%" :modal="true">
      <el-form label-width="80px" size="small">
        <el-upload
            class="avatar-uploader"
            action="http://localhost:8081/file/upload"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
        >
          <img v-if="form.avatarurl" :src="form.avatarurl" class="avatar" alt="头像">
          <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload>
        <el-form-item label="用户名">
          <el-input v-model="form.username" autocomplete="off" disabled></el-input>
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="form.nickname" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="changeinformation = false">取 消</el-button>
        <el-button type="primary" @click="update">确认修改</el-button>
      </div>
    </el-dialog>






  </div>
</template>

<script>
import Cookies from 'js-cookie'
export default {
  name: "Header",
  props: {
    collapseBtnClass: String,
  },
  computed: {
    currentPathName () {
      return this.$store.state.currentPathName;　　//需要监听的数据

    }
  },
  data() {
    return {
      user:Cookies.get('cookie')? JSON.parse(Cookies.get('cookie')):{},
      form: {},
      userpassword:{
        username:"",
        password:"",
        newpassword:"",
        repassword:"",
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
      changeinformation:false,
    }
  },
  created(){
  this.load()

  },
  methods: {
    load(){
      const username =this.user.username
      if(!username){
        this.$message.error("无法获取用户信息1")
        return
      }
      this.request.get("/user/username/"+username).then(res=>{
        this.form=res.data
        this.user=res.data
      })
    },
    update(){
      this.request.post("/user/update", this.form).then(res => {
        if (res.code === '200') {
          this.$message.success(res.msg)
          this.changeinformation=false
          this.load()
        }else{
          this.$message.error(res.msg)
        }
      })

    },

    handleAvatarSuccess(res){
      this.form.avatarurl=res.data
    },
    collapse() {
      this.$emit("pcollapse")
    },
    logout() {
      this.request.post("/user/logout/" + this.user.uid).then(res => {

      })
      this.$store.commit("logout")
      this.$message.success("退出成功")

    },
    changepasswd(){
      this.$refs['userpasswordForm'].validate((valid) => {
        if (valid) {  // 表单校验合法
          if(this.userpassword.newpassword !==this.userpassword.repassword)
          {
            this.$message.error("两次密码不一致")
            return false;
          }
          this.request.post("/user/changepassword",this.userpassword).then(res =>{
            if(res.code === "200") {
              this.$message.success(res.msg)
              this.userpassword.newpassword=null
              this.userpassword.password=null
              this.userpassword.repassword=null
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
  }
}
</script>

<style scoped>
.avatar-uploader {
  text-align: center;
  padding-bottom: 10px;

}
/deep/ .avatar-uploader   .el-upload   {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload :hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 138px;
  height: 138px;
  line-height: 138px;
  text-align: center;
}
.avatar {
  width: 138px;
  height: 138px;
  display: block;
}
</style>
