<template>
<div style="position: absolute; width: 90%">
  <div style="margin: 20px 150px">
    <el-input style="width: 400px;margin-right: 40px;margin-left: 100px" placeholder="请输入待提交的flag" v-model="flagForm.flag"></el-input>
    <el-button type="primary" @click="upFlag"  size="large">提交flag</el-button>
  </div>
  <div style="height:500px;width:1000px;border:2px solid #479de1;position: relative;">
    <div v-for="item in down" :key="item.id" class="alert1"  :style="{left: item.positionX/657*1000+ 'px', top: item.positionY/300*500+ 'px'}">
      <img :src="item.imageUrl" style="width:40px; height: 40px ;position: absolute" >
      <el-tag size="medium"  type="warning" style="margin-top: 40px">
        {{item.name}}
      </el-tag>
    </div>
    <div v-for="item in notDown" :key="item.id" class="alert1"  :style="{left: item.positionX/657*1000+ 'px', top: item.positionY/300*500+ 'px'}">
      <img :src="item.imageUrl" style="width:40px; height: 40px ;position: absolute" >
      <el-tag size="medium"  type="success" style="margin-top: 40px">
        {{item.name}}
      </el-tag>
    </div>
    <img :src="rangce.background" style="width:100%; height: 100%" >
  </div>

  <div style="position: absolute; top:30%;right: 3%; ">
    <el-table   :data="hostList" border stripe :header-cell-class-name="'headerBg'" >
      <el-table-column prop="name" label="名称" width="200"  align="center"></el-table-column>
      <el-table-column prop="status" label="状态与操作"width="100" align="center" >
        <template v-slot="scope">
          <el-dropdown style="margin-left: 6px">
            <div style="display: inline-block">
              <el-tag size="medium"  type="info" v-if="scope.row.status==='Build'">创建中</el-tag>
              <el-tag size="medium"  type="info" v-if="scope.row.status==='Starting'">启动中</el-tag>
              <el-tag size="medium"  type="info" v-if="scope.row.status==='Shuting'">关机中</el-tag>
              <el-tag size="medium"  type="success" v-if="scope.row.status==='Active'">运行中</el-tag>
              <el-tag size="medium"  type="danger" v-if="scope.row.status==='Shutoff'">关机</el-tag>
            </div>
            <el-dropdown-menu slot="dropdown" style="width: 100px; text-align: center">
              <el-dropdown-item style="font-size: 14px; padding: 5px 0" v-if="scope.row.status==='Active'">
                <span @click="stopHost(scope.row.id)">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp关机&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp</span>
              </el-dropdown-item>
              <el-dropdown-item style="font-size: 14px; padding: 5px 0" v-if="scope.row.status==='Shutoff'">
                <span @click="startHost(scope.row.id)">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp开机&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp</span>
              </el-dropdown-item>
              <el-dropdown-item style="font-size: 14px; padding: 5px 0" v-if="scope.row.status==='Active'" @click="useHost(scope.row)">
                <span  @click="useHost(scope.row.id)">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp使用&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp</span>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>
  </div>

</div>
</template>

<script>
import Cookies from "js-cookie";

export default {
  name: "RangceLarge" ,
  data() {
    return {
      down:[],
      notDown:[],
      flagForm:{},
      hostList:[],
      rangceId:this.$route.query.rangceId,
      rangce:{},
      user: Cookies.get('cookie')? JSON.parse(Cookies.get('cookie')):{},
    }
  },
  created() {
    this.load()
  },
  methods: {
    load(){
      this.request.get("/rangce/" + this.rangceId).then(res => {
        this.rangce = res.data
      })
      this.getList()
      this.getVisMap()
      this.getDownList()
    },

    getList() {
      this.request.get("/host/list",  {
        params: {
          rangceId:this.rangceId,
          userId:this.user.uid,
          type:0
        }
      }).then(res => {
        if (res.code === '200') {
          this.hostList=res.data
        } else {
        }
      })
    },
    getVisMap() {
      this.request.get("/rangce-host/map",  {
        params: {
          rangceId:this.rangceId,
          userId:this.user.uid,
        }
      }).then(res => {
        if (res.code === '200') {
          this.nodes=res.data.nodes
          this.edges=res.data.edges
        } else {
        }
      })
    },
    upFlag() {
      this.flagForm.userId=this.user.uid
      this.flagForm.rangceId=this.rangceId
      this.request.post("/flag/save",this.flagForm ).then(res => {
        if (res.code === '200') {
          this.flagForm.flag="";
          this.$message.success(res.msg)
          this.getVisMap()
          this.getDownList()
        } else {
          this.$message.error(res.msg)
        }
      })
    },

    getDownList(){
      this.request.get("/rangce-host/getDownList", {
        params: {
          userId:this.user.uid,
          rangceId:this.rangceId
        }
      }).then(res => {
        this.down=res.data.downList
        this.notDown=res.data.notDownList
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
          this.getList()
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
          this.getList()
        } else {
          this.$message.error(res.msg)
        }
      })

    },



  }
}
</script>

<style scoped>
.alert{
  /*这个样式不写，右键弹框会一直显示在画布的左下角*/
  display: none;
  position: absolute;
  font-size: 20px;
  background: rgb(71, 157, 225);
  border-radius: 5px;
  z-index: 10;
}
.alert1{
  /*这个样式不写，右键弹框会一直显示在画布的左下角*/
  position: absolute;
  opacity: 0.8;
  font-size: 10px;
  border-radius: 5px;
  z-index: 10;
}
</style>