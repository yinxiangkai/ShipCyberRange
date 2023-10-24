<template>
  <div style="position: relative;">
    <el-card style="position: relative;height:440px;margin: 0 auto">
      <div class="picture">
        <img :src="rangce.url" style="width:100%; height: 100%" >
      </div>
      <div class="content">
        <el-tabs type="card" v-model="activeName" @tab-click="handleClick" >
          <el-tab-pane label="靶场信息" name="靶场信息" style="height:300px;overflow: auto;border:2px solid #479de1" >
            <div>
              <b style="color: #3c3f41;font-size: 20px;margin-bottom: 5px">靶场名称：{{rangce.name}}</b>
            </div>
            <div>
              <b style="color: #3c3f41;font-size: 20px;margin-bottom: 5px">靶场介绍：</b>
            </div>
            <div v-html="rangce.introduction" style="margin-left: 90px; "></div>
          </el-tab-pane>
          <el-tab-pane label="靶场使用"  name="靶场使用"  style="height:300px; overflow: auto;border:2px solid #479de1">
            <div v-if="role===1">
              <el-button type="primary" @click="abandon" style="margin-left: 50px;margin-top: 50px" size="large">释放靶场</el-button>
              <el-button type="primary" @click="goRangce" style="margin-left: 50px;margin-top: 50px" size="large">使用靶场</el-button>
              <div style="margin-top: 50px">
                <el-input style="width: 400px;margin-right: 20px;margin-left: 50px" placeholder="请输入待提交的flag" v-model="flagForm.flag"></el-input>
                <el-button type="primary" @click="upFlag"  size="large">提交flag</el-button>
              </div>
            </div>
            <div v-if="role===0">
              <el-button type="primary" @click="apply" style="margin-left: 50px;margin-top: 50px" size="large">申请靶场</el-button>
            </div>
            <div v-if="role===2">
            <el-button type="primary"  style="margin-left: 50px;margin-top: 50px" size="large">靶场申请中</el-button>
          </div>

          </el-tab-pane>
          <el-tab-pane label="靶机列表" v-if="role===1" name="靶机列表"  style="height:300px; overflow: auto;border:2px solid #479de1">
            <el-table   :data="hostList" border stripe :header-cell-class-name="'headerBg'" >
              <el-table-column prop="name" label="名称"></el-table-column>
              <el-table-column prop="status" label="状态">
                <template v-slot="scope">
                  <el-tag size="medium"  type="info" v-if="scope.row.status==='Build'">创建中</el-tag>
                  <el-tag size="medium"  type="info" v-if="scope.row.status==='Starting'">启动中</el-tag>
                  <el-tag size="medium"  type="info" v-if="scope.row.status==='Shuting'">关机中</el-tag>
                  <el-tag size="medium"  type="success" v-if="scope.row.status==='Active'">运行中</el-tag>
                  <el-tag size="medium"  type="danger" v-if="scope.row.status==='Shutoff'">关机</el-tag>
                </template>
              </el-table-column>
              <el-table-column label="操作"  width="260" align="center">
                <template v-slot="scope">
                  <el-dropdown style="margin-left: 6px">
                    <div style="display: inline-block">
                      <el-button type="primary" slot="reference">操作主机</el-button>
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
          </el-tab-pane>
          <el-tab-pane label="网络拓扑" name="网络拓扑" v-if="role===1" style="height:300px; overflow: auto;border:2px solid #479de1"  >
            <div  id="network" style="width: 700px;height: 400px"  @mousemove='updatePosition'></div>
            <div class="alert" id="alert"  :style="{left: transLeft, top: transTop} ">
            </div>
          </el-tab-pane>
          <el-tab-pane label="船舶控制台" name="船舶控制台" v-if="role===1" style="height:300px;border:2px solid #479de1"  >
              <el-tag size="medium"  type="warning" v-for="item in down" :key="item.id" class="alert1"  :style="{left: item.positionX + 'px', top: item.positionY+ 'px'}">
                {{item.name}}
              </el-tag>
              <el-tag size="medium"  type="success" v-for="item in notDown" :key="item.id" class="alert1"  :style="{left: item.positionX + 'px', top: item.positionY+ 'px'}">
                {{item.name}}
              </el-tag>
              <img :src="rangce.background" style="width:100%; height: 100%" >
          </el-tab-pane>
        </el-tabs>

      </div>
    </el-card>




    <!--发表评论 -->
    <div style="margin: 30px 0">
      <div style="margin: 10px 0">
        <div style="border-bottom: 1px solid #479de1; padding: 10px 0; font-size: 20px"><b style="color: #3a8ee6">评论</b></div>
        <div style="padding: 10px 0">
          <el-input size="small" type="textarea" v-model="commentForm.content"></el-input>
        </div>
        <div class="pd-10" style="text-align: right">
          <el-button type="primary" size="small" @click="save">评论</el-button>
        </div>
      </div>

      <!--      评论列表-->
        <el-card v-for="item in comments" :key="item.id" style="border-bottom: 1px solid #ccc; ">
          <div style="display: flex">
            <div style="width: 100px; text-align: center">
              <el-image :src="item.avatarurl" style="width: 50px; height: 50px; border-radius: 50%"></el-image>
            </div> <!--  头像-->
            <div style="flex: 1; font-size: 14px; padding: 5px 0; line-height: 25px">
              <b>{{ item.nickname }}：</b>
              <span>{{ item.content }}</span>

              <div style="display: flex; line-height: 20px; margin-top: 5px">
                <div style="width: 200px;">
                  <i class="el-icon-time"></i><span style="margin-left: 5px">{{ item.time }}</span>
                </div>
                <div style="text-align: right; flex: 1">
                  <el-button style="margin-left: 5px" type="text" @click="handleReply(item.id)"><b style="color: #3a8ee6;font-size: 14px">回复</b></el-button>
                  <el-button type="text" style="color: red" @click="del(item.id)" v-if="user.uid === item.userId"><b style="color: #e63a54;font-size: 14px">删除</b></el-button>
                </div>
              </div>
            </div>
            <!--  内容-->
          </div>

          <!--回复列表-->
          <div v-if="item.children.length"  style="padding-left: 150px;">
            <div v-for="subItem in item.children" :key="subItem.id"  style="background-color: #ffffff; padding: 5px 20px">
              <div style="display: flex">
                <div style="text-align: center">
                  <el-image :src="subItem.avatarurl" style="width: 50px; height: 50px; border-radius: 50%"></el-image>
                </div> <!--  头像-->
                <div style="flex: 1; font-size: 14px;margin-left: 5px; line-height: 25px">
                  <b>{{ subItem.nickname }}</b>
                  <br>
                  <i class="el-icon-time"></i>
                  <span>{{ subItem.time }}</span>
                  <div  v-if="subItem.pnickname">
                    <b>回复<b style="color: #3a8ee6"> @{{ subItem.pnickname }}</b>：</b>
                    <span>{{ subItem.content }}</span>
                  </div>
                  <div style="text-align: right;margin-top: 5px;">
                    <el-button style="margin-left: 5px" type="text" @click="handleReply(subItem.id)"><b style="color: #3a8ee6;font-size: 14px">回复</b></el-button>
                    <el-button type="text"  @click="del(subItem.id)" v-if="user.uid === subItem.userId"><b style="color: #e63a54;font-size: 14px">删除</b></el-button>
                  </div>
                </div>   <!--  内容-->
              </div>
              <hr>
            </div>
          </div>
        </el-card>
    </div>


    <el-dialog title="回复" :visible.sync="dialogFormVisible" width="50%" >
      <el-form label-width="80px" size="small">
        <el-form-item label="回复内容">
          <el-input type="textarea" v-model="commentForm.contentReply" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false" size="small">取 消</el-button>
        <el-button type="primary" @click="save"  size="small">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import Cookies from "js-cookie";
import { DataSet, Network } from 'vis';
export default {
  name: "Rangce",
  data() {
    return {
      down:[],
      notDown:[],
      role:0,
      comments: [],
      rangce:{},
      activeName:"靶场信息",
      commentForm: {},
      flagForm:{},
      hostList:[],
      rangceId:this.$route.query.id,
      dialogFormVisible: false,
      user: Cookies.get('cookie')? JSON.parse(Cookies.get('cookie')):{},
      nodes:[],
      edges:[],
      boxShow:false,
      transLeft:0,
      transTop:0,
    }
  },
  created() {
    this.load()
    this.loadComment()
    this.getRole()
    this.getList()
    this.timeGet()
    this.getVisMap()
    this.getDownList()
  },
  methods: {
    load() {
      this.request.get("/rangce/" + this.rangceId).then(res => {
        this.rangce = res.data
      })
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
    loadComment() {
      this.request.get("/comment/tree/" + this.rangceId).then(res => {
        this.comments = res.data
      })
    },
    save() {
      if (!this.user.uid) {
        this.$message.warning("请登录后操作")
        return
      }
      this.commentForm.rangceId = this.rangceId
      if (this.commentForm.contentReply) {
        this.commentForm.content = this.commentForm.contentReply
      }
      this.request.post("/comment", this.commentForm).then(res => {
        if (res.code === '200') {
          this.$message.success("评论成功")
          this.commentForm = {}  // 初始化评论对象内容
          this.loadComment()
          this.dialogFormVisible = false
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    del(id) {
      this.request.delete("/comment/" + id).then(res => {
        if (res.code === '200') {
          this.$message.success("删除成功")
          this.loadComment()
        } else {
          this.$message.error("删除失败")
        }
      })
    },
    handleReply(pid) {
      this.commentForm = { pid: pid }
      this.dialogFormVisible = true
    },
    getRole() {
      this.request.get("/rangce-group/role",  {
        params: {
          rangceId:this.rangceId,
          uid:this.user.uid
        }
      }).then(res => {
        if (res.code === '200') {
           this.role=res.data
        } else {
        }
      })
    },
    apply() {
      this.request.get("/rangce-group/apply",  {
        params: {
          uid:this.user.uid,
          rangceId:this.rangceId,
        }
      }).then(res => {
        if (res.code === '200') {
          this.$store.state.task.push({
            id: res.data.id,
            status:res.data.status
          })
          this.getRole()
          this.$message.success(res.msg)
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    abandon() {
      this.request.get("/rangce-group/abandon",  {
        params: {

          uid:this.user.uid,
          rangceId:this.rangceId,
        }
      }).then(res => {
        if (res.code === '200') {
          this.role=res.data
          this.getRole()
          this.$message.success(res.msg)
        } else {
          this.$message.error(res.msg)
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
    timeGet(){
     setInterval(this.getStatus, 20000); // 注意: 第一个参数为方法名的时候不要加括号;
    },
    getStatus(){
      this.$store.state.task.forEach(element=>{
        if(element.status==="UNFINISHED"){
          this.request.get("/task/status/"+element.id).then(res => {
            element.status=res.data
            if(element.status==="FINISHED"){
              this.getList()
              this.getRole()
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
    updatePosition:function(event){
     this.transLeft=event.offsetX+40+'px';
     this.transTop=event.offsetY+40+'px';
    },
    handleClick(){
      if(this.activeName==="网络拓扑"){
        this.loadEcharts()
      }
    },

    loadEcharts(){
     let container =document.getElementById('network');
     let data = {
        nodes: this.nodes,
        edges: this.edges
      };

     let options = {
        //节点样式
        nodes: {
          chosen: true, // 对选择节点做出反应
          color: {
            border: '#479de1',
            background: '#97C2FD',
            highlight: {
              border: '#2B7CE9',
              background: '#ffffff'
            },
            hover: {
              border: '#2B7CE9',
              background: '#ade3b2'
            }
          },
          font: {
            color: '#3c3f41',
            size: 20
          },
          labelHighlightBold: false,
          shape: "image",//设置节点node样式为矩形
          scaling: {
            min: 16,
            max: 32 //缩放效果比例
          },
        },
        //连接线的样式
        edges: {
          color: {
            color: "rgb(97, 168, 224)",
            highlight: "rgb(97, 168, 224)",
            hover: "green",
            inherit: "from",
            opacity: 1.0
          },
          font: {
            align: "top",//连接线文字位置
          },
          smooth: true, //是否显示方向箭头
          arrows: {to : true },//箭头指向from节点
        },

        layout: {
          //以分层方式定位节点
          hierarchical: {
            direction: "LR",//分层排序方向
            sortMethod: 'directed',
            levelSeparation:200//不同级别之间的距离
          },
        },
        interaction: {
          hover: true, // 启用鼠标悬停
          hoverConnectedEdges: false, // 鼠标悬停在节点上时，与其连接的边不高亮显示
          hideEdgesOnDrag: false, // true时拖动视图时不会绘制边。这会加快拖动时的响应速度
          hideNodesOnDrag: false, // true时拖动视图时不会绘制节点
          navigationButtons: true,
          selectConnectedEdges: false, // 选中节点时，与其连接的边不高亮
          multiselect: false, // true时长时间单击（或触摸）以及控件单击将添加到选择中
          tooltipDelay: 100,
        },
      };
      this.network = new Network(container, data, options);
      function getNode(option) {
        for (let i = 0;i < data.nodes.length; i++){
          if (option === data.nodes[i].id){
            return data.nodes[i];
          }
        }
      }
      this.network.on('hoverNode',function(properties){
        let hoveNodeList = getNode(properties.node);
        let ipaddr = hoveNodeList.ipaddr;
        let object = document.getElementById('alert')
        object.innerText=ipaddr
        object.style.display="block"
      });

      this.network.on('blurNode',function(){
        let object = document.getElementById('alert')
        object.style.display="none"
      });

    },
    goRangce() {
      this.$router.push({
        path: '/Uhome/rangce_large',
        query: {
          rangceId: this.rangceId,
        }
      })
    },

  }
}
</script>

<style scoped>
.picture{
  position: absolute;
  height:80%;
  width:30%;
  top:10%;
  left:5%;
}
.content{
  position: absolute;
  height:80%;
  width:55%;
  top:10%;
  left:40%;
}
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