<template>
  <div>
    <div style="margin: 10px 0">
      <el-input style="width: 200px ;margin: 5px" placeholder="请输入主机名称" suffix-icon="el-icon-search" v-model="name"></el-input>
      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
      <el-button type="success" @click="search">高级搜索</el-button>
    </div>

    <el-table   v-loading="loading" :data="tableData" border stripe :header-cell-class-name="'headerBg'" >
      <el-table-column prop="id" label="ID" width="80" sortable></el-table-column>
      <el-table-column prop="name" label="名称">
        <template v-slot="scope">
          <span @click="goHost(scope.row.id)" style="color: #409EFF">{{ scope.row.name}}</span>
        </template>
      </el-table-column>
      <el-table-column prop="ipaddr" label="IP地址"></el-table-column>
      <el-table-column prop="status" label="状态">
        <template v-slot="scope">
          <el-tag size="medium"  type="info" v-if="scope.row.status==='Build'">创建中</el-tag>
          <el-tag size="medium"  type="info" v-if="scope.row.status==='Starting'">启动中</el-tag>
          <el-tag size="medium"  type="info" v-if="scope.row.status==='Shuting'">关机中</el-tag>
          <el-tag size="medium"  type="success" v-if="scope.row.status==='Active'">运行中</el-tag>
          <el-tag size="medium"  type="danger" v-if="scope.row.status==='Shutoff'">关机</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="type" label="类型">
        <template v-slot="scope">
          <el-tag size="medium"  type="success" v-if="scope.row.type===0">用户可见</el-tag>
          <el-tag size="medium"  type="warning" v-if="scope.row.type===1">用户不可见</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createtime" label="创建时间"></el-table-column>
      <el-table-column prop="grade" label="分数"></el-table-column>
      <el-table-column prop="flag" label="Flag码"></el-table-column>
      <el-table-column label="操作"  width="260" align="center">
        <template v-slot="scope">
          <el-dropdown style="margin-left: 6px">
            <div style="display: inline-block">
              <el-button type="primary" slot="reference">操作主机</el-button>
            </div>
            <el-dropdown-menu slot="dropdown" style="width: 100px; text-align: center">
              <el-dropdown-item style="font-size: 14px; padding: 5px 0" v-if="scope.row.status==='Active'">
                <span @click="stopHost(scope.row.id)">关机</span>
              </el-dropdown-item>
              <el-dropdown-item style="font-size: 14px; padding: 5px 0" v-if="scope.row.status==='Shutoff'">
                <span @click="startHost(scope.row.id)">开机</span>
              </el-dropdown-item>
              <el-dropdown-item style="font-size: 14px; padding: 5px 0" v-if="scope.row.status==='Active'" @click="useHost(scope.row)">
                <span  @click="useHost(scope.row.id)">使用</span>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
          <el-popconfirm
              class="ml-5"
              confirm-button-text='确定'
              cancel-button-text='我再想想'
              icon="el-icon-info"
              icon-color="red"
              title="您确定删除吗？"
              @confirm="del(scope.row.id)"
          >
            <el-button type="danger" slot="reference" >删除 <i class="el-icon-remove-outline"></i></el-button>
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
    <el-dialog title="高级搜索" :visible.sync="dialogFormVisible" width="25%" :close-on-click-modal="false">
      <el-form>
        <el-form-item label="主机名称">
          <el-input style="width: 200px" placeholder="请输入主机名称" suffix-icon="el-icon-search" v-model="name"></el-input>
        </el-form-item>
        <el-form-item label="用户名称">
          <el-input style="width: 200px" placeholder="请输入用户名称" suffix-icon="el-icon-search" v-model="userName"></el-input>
        </el-form-item>
        <el-form-item label="靶场名称">
          <el-input style="width: 200px" placeholder="请输入靶场名称" suffix-icon="el-icon-search" v-model="rangceName"></el-input>
        </el-form-item>
        <el-form-item label="子网名称">
          <el-input style="width: 200px" placeholder="请输入子网名称" suffix-icon="el-icon-search" v-model="subnetName"></el-input>
        </el-form-item>
        <el-form-item label="主机地址">
          <el-input style="width: 200px" placeholder="请输入IP地址" suffix-icon="el-icon-search" v-model="ipaddr"></el-input>
        </el-form-item>
        <el-form-item label="靶场编号">
          <el-input style="width: 200px" placeholder="请输入靶场编号" suffix-icon="el-icon-search" v-model="no"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="load">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import Cookies from "js-cookie";

export default {
  name: "HostManager",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      form: {},
      name:"",
      userName:"",
      rangceName:"",
      subnetName:"",
      no:"",
      ipaddr:"",
      status:"DOWN",
      loading:false,
      multipleSelection: [],
      dialogFormVisible:false,
      user: Cookies.get('cookie')? JSON.parse(Cookies.get('cookie')):{},
    }
  },
  created() {
    this.load()
    this.timeGet()
  },
  methods: {

    load() {
      this.dialogFormVisible = false
      this.request.get("/host/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
          userName:this.userName,
          rangceName:this.rangceName,
          subnetName:this.subnetName,
          no:this.no,
          ipaddr: this.ipaddr,
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    del(id) {
      this.request.delete("/host/del/" + id).then(res => {
        if (res.code === '200') {
          this.$message.success(res.msg)
          this.load()
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    reset() {
      this.name = ""
      this.userName="",
      this.rangceName="",
      this.subnetName="",
      this.no="",
      this.ipaddr="",
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
    timeGet(){
       setInterval(this.getStatus, 60000); // 注意: 第一个参数为方法名的时候不要加括号;
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
    useHost(id){
      this.request.get("/host/use/" + id).then(res => {
        if (res.code === '200') {
          if(res.data!==null){
            window.open(res.data,'_blank');
          }
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    stopHost(id){
      this.request.get("/host/stop/" + id).then(res => {
        if (res.code === '200') {
          if(res.data!==null){
            this.$store.state.task.push({
              id: res.data.id,
              status:res.data.status
            })
          }
          this.$message.success(res.msg)
          this.load()
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    startHost(id){
      this.request.get("/host/start/" + id).then(res => {
        if (res.code === '200') {
          if(res.data!==null){
            this.$store.state.task.push({
              id: res.data.id,
              status:res.data.status
            })
          }
          this.$message.success(res.msg)
          this.load()
        } else {
          this.$message.error(res.msg)
        }
      })

    },
    search(){
      this.dialogFormVisible=true
    },
    goHost(id) {
      this.$router.push({
        path: '/Ahome/host_detail',
        query: {
          id: id,
        }
      })
    },

  }
}
</script>

<style scoped>

</style>