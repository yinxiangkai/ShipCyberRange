<template>
  <div id="login">
    <div id="logo">
      <div id="logo-text">
        虚拟船舶靶场
        <div id="logo-image">
          <img width="60" src="../../assets/logo.jpg" alt="login" id="logo-image"  >
        </div>
      </div>
      <div id="login-1">
        <el-card class="box-card" shadow="hover"  >
          <div style="margin: 20px 0; text-align: center; font-size: 24px"><b>登 录</b></div>
          <div class="form">
            <el-form :model="user" :rules="rules" ref="userForm" >
              <el-form-item prop="username">
                <el-input  placeholder="请输入账号"  v-model="user.username"   clearable></el-input>
              </el-form-item>
              <el-form-item prop="password">
                <el-input placeholder="请输入密码" v-model="user.password" show-password></el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" class="button" @click="login">登录</el-button>
                <el-link type="primary" @click="dialogFormVisible = true" class="link">申请账号</el-link>
              </el-form-item>
            </el-form>
          </div>
        </el-card>
        <div id="window">
          <el-dialog title="注册账号" :visible.sync="dialogFormVisible" width="30%" :modal="true" :modal-append-to-body='false'>
            <el-form :model="user" :rules="rules" ref="userForm">
              <el-form-item prop="username">
                <el-input  placeholder="请输入账号"  v-model="user.username"  clearable></el-input>
              </el-form-item>
              <el-form-item prop="password">
                <el-input placeholder="请输入密码" v-model="user.password" show-password></el-input>
              </el-form-item>
              <el-form-item prop="repassword">
                <el-input placeholder="请确认密码" v-model="user.repassword" show-password></el-input>
              </el-form-item>
              <el-form-item prop="email">
                <el-input  placeholder="请输入邮箱"  v-model="user.email "  clearable></el-input>
              </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
              <el-button @click="dialogFormVisible = false">取 消</el-button>
              <el-button type="primary" @click="register">注册</el-button>
            </div>
          </el-dialog>
        </div>
      </div>
    </div>
  </div>

</template>

<script>
import Cookies from 'js-cookie'
export default {
  name: "Login",
  data() {
    return {
      user:{
        username :'',
        password :"",
        email:"",
        address:"",
        repassword:"",
      },
      rules: {

        username: [
          {required: true, message: '未输入用户名', trigger: 'blur'},
          {min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur'},
        ],
        password: [
          {required: true, message: '未输入密码', trigger: 'blur'},
          {min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur'},
        ],
        repassword: [
          {required: true, message: '未确认密码', trigger: 'blur'},
        ],
        email:[
          { required: true, message: '请输入邮箱地址', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change']}
        ]
      },
      dialogFormVisible: false,
    }

  },
  methods:{

    register() {
      this.$refs['userForm'].validate((valid) => {
        if (valid) {  // 表单校验合法
          if (this.user.password !== this.user.repassword) {
            this.$message.error("两次输入的密码不一致")
            return false
          }
          this.request.post("/user/register",this.user).then(res =>{
            if(res.code==="200"){
              this.$message.success("注册成功")
              this.dialogFormVisible=false
            }else
            {
              this.$message.error(res.msg)
            }
          })
        } else {
          return false;
        }
      })

    },
    login() {
      this.$refs['userForm'].validate((valid) => {
        if (valid) {  // 表单校验合法
          this.request.post("/user/login",this.user).then(res =>{
            if(res.code === "200") {
              if(res.data!==null){
                Cookies.set('cookie',JSON.stringify(res.data))
                localStorage.setItem("cookie", JSON.stringify(res.data))  // 存储用户信息到浏览器
                if(res.data.role===0){
                  this.$router.push("/Shome")
                }else if (res.data.role===1){
                  this.$router.push("/Ahome")
                }else{
                  this.$router.push("/Uhome")
                }
              }
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

<style scoped lang="less">
#login {
  position:fixed;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
  background-image: linear-gradient(90deg,#92b2e1,cyan);
  background-size: 400%;
  animation: anim-login 10s infinite;
}
@keyframes anim-login {
  0%{
    background-position: 0 50%;
  }
  50%{
    background-position: 100% 50%;
  }
  100%{
    background-position: 0 50%;
  }
}
#logo-text{
  color: aliceblue;
  font-size:26px;
  font-weight: 800;
  position: absolute;
  top: 15%;
  left: 15%;

}
#logo-image{
  position: absolute;
  top: -50%;
  left: 110%;
}
#login-1
{
  position: absolute;
  top: 30%;
  left:40%;
  .box-card {
    width: 340px;
    height: 280px;
    background-color: rgba(255,255,255,0.6);
    border-radius: 15px;
  }
  .form{

    line-height: 50px;
    padding: 15px;
    margin-top:-10px ;


  }

  .button{
    margin-left: 35%;
  }
  .link{
    margin-top: -35%;
    margin-left: 75%;
  }
}

</style>