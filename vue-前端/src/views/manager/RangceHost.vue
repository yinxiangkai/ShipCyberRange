<template>
  <div>
    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="name"></el-input>
      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
      <el-button type="primary" @click="handleAdd">新增 <i class="el-icon-circle-plus-outline"></i></el-button>
    </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'" >
      <el-table-column prop="id" label="ID" width="80" sortable align="center"></el-table-column>
      <el-table-column prop="name" label="名称" align="center"></el-table-column>
      <el-table-column prop="ipaddr" label="IP地址" align="center"></el-table-column>
      <el-table-column prop="number" label="靶机组内编号" align="center"></el-table-column>

      <el-table-column prop="status" label="状态" align="center">
        <template v-slot="scope">
          <el-tag size="medium"  type="info" v-if="scope.row.status==='Build'">创建中</el-tag>
          <el-tag size="medium"  type="info" v-if="scope.row.status==='Starting'">启动中</el-tag>
          <el-tag size="medium"  type="info" v-if="scope.row.status==='Shuting'">关机中</el-tag>
          <el-tag size="medium"  type="success" v-if="scope.row.status==='Active'">运行中</el-tag>
          <el-tag size="medium"  type="danger" v-if="scope.row.status==='Shutoff'">关机</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="type" label="类型" align="center">
        <template v-slot="scope">
          <el-tag size="medium"  type="success" v-if="scope.row.type===0">用户可见</el-tag>
          <el-tag size="medium"  type="warning" v-if="scope.row.type===1">用户不可见</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createtime" label="创建时间"  width="150" align="center"></el-table-column>
      <el-table-column prop="grade" label="分数" align="center"></el-table-column>
      <el-table-column prop="flag" label="flag码" align="center">
        <template v-slot="scope">
          <el-popover trigger="hover" placement="top">
            <div v-html="scope.row.flag"></div>
            <div slot="reference" class="name-wrapper">
              <el-tag size="medium">flag码</el-tag>
            </div>
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column label="操作"  width="260" align="center">
        <template v-slot="scope">
          <el-button type="success" @click="handleEdit(scope.row)">编辑 <i class="el-icon-edit"></i></el-button>
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
    <el-dialog title="新增主机" :visible.sync="dialogFormVisible" width="60%" :close-on-click-modal="false">
      <el-tabs tab-position="left">
        <el-tab-pane label="信息设置">
          <el-form label-width="100px" size="small" style="width: 90%">
            <el-form-item label="主机名称">
              <el-input v-model="form.name" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="主机flag码">
              <el-input v-model="form.flag" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="分数">
              <el-input v-model="form.grade" type="number" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="靶机组内编号">
              <el-input v-model="form.number" type="number" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="靶机类型">
              <el-select v-model="form.type" placeholder="请选择主机类型">
                <el-option label="用户可见" value=0></el-option>
                <el-option label="用户不可见" value=1></el-option>
              </el-select>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="镜像设置">
          <div style="margin: 10px 0">
            <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="nameImage"></el-input>
            <el-button class="ml-5" type="primary" @click="loadImage">搜索</el-button>
            <el-button type="warning" @click="resetImage">重置</el-button>
            <span style="margin-left: 40px;">当前选择的镜像:<span style="color: #757474;font-size: 20px">{{this.imageName}}</span></span>
          </div>
          <div class="table">
            <el-table :data="imageData" border stripe :header-cell-class-name="'headerBg'" >
              <el-table-column prop="id" label="镜像编号" width="80"></el-table-column>
              <el-table-column prop="name" label="镜像名称"></el-table-column>
              <el-table-column prop="time" label="创建时间"></el-table-column>
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
              <el-table-column label="操作"  width="120" align="center">
                <template v-slot="scope">
                  <el-button type="success" @click="selectImage(scope.row)">选择<i class="el-icon-edit"></i></el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
          <div class="block">
            <el-pagination
                @size-change="handleSizeChangeImage"
                @current-change="handleCurrentChangeImage"
                :current-page="pageNumImage"
                :page-sizes="[2, 5]"
                :page-size="pageSizeImage"
                layout="total, sizes, prev, pager, next, jumper"
                :total="totalImage">
            </el-pagination>
          </div>
        </el-tab-pane>
        <el-tab-pane label="规格设置">
          <div style="margin: 10px 0">
            <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="nameFlavor"></el-input>
            <el-button class="ml-5" type="primary" @click="loadFlavor">搜索</el-button>
            <el-button type="warning" @click="resetFlavor">重置</el-button>
            <span style="margin-left: 40px;">当前选择的规格:<span style="color: #757474;font-size: 20px">{{this.flavorName}}</span></span>
          </div>
          <div class="table">
            <el-table :data="flavorData" border stripe :header-cell-class-name="'headerBg'" >
              <el-table-column prop="id" label="ID" width="80" sortable></el-table-column>
              <el-table-column prop="name" label="类型名称"></el-table-column>
              <el-table-column prop="ram" label="内存MB"></el-table-column>
              <el-table-column prop="vcpus" label="CPU个数"></el-table-column>
              <el-table-column prop="disk" label="硬盘大小GB"></el-table-column>
              <el-table-column label="操作"  width="120" align="center">
                <template v-slot="scope">
                  <el-button type="success" @click="selectFlavor(scope.row)">选择<i class="el-icon-edit"></i></el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
          <div class="block">
            <el-pagination
                @size-change="handleSizeChangeFlavor"
                @current-change="handleCurrentChangeFlavor"
                :current-page="pageNumFlavor"
                :page-sizes="[2, 5]"
                :page-size="pageSizeFlavor"
                layout="total, sizes, prev, pager, next, jumper"
                :total="totalFlavor">
            </el-pagination>
          </div>
        </el-tab-pane>
        <el-tab-pane label="网络设置">
          <div style="margin: 10px 0">
            <el-input style="width: 200px" placeholder="请输入IP" suffix-icon="el-icon-search" v-model="ipaddr"></el-input>
            <el-button class="ml-5" type="primary" @click="loadPort">搜索</el-button>
            <el-button type="warning" @click="resetPort">重置</el-button>
            <span style="margin-left: 40px;">当前选择的端口：<span style="color: #757474;font-size: 20px">{{this.portName}}</span></span>
          </div>
          <div class="table">
            <el-table :data="portData" border stripe :header-cell-class-name="'headerBg'" >
              <el-table-column prop="id" label="ID" width="80" sortable></el-table-column>
              <el-table-column prop="name" label="端口名称"></el-table-column>
              <el-table-column prop="ipaddr" label="IP地址"></el-table-column>
              <el-table-column prop="subnet" label="所属子网"></el-table-column>
              <el-table-column prop="network" label="所属网络"></el-table-column>
              <el-table-column label="操作"  width="120" align="center">
                <template v-slot="scope">
                  <el-button type="success" @click="selectPort(scope.row)">选择<i class="el-icon-edit"></i></el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
          <div class="block">
            <el-pagination
                @size-change="handleSizeChangePort"
                @current-change="handleCurrentChangePort"
                :current-page="pageNumPort"
                :page-sizes="[2, 5]"
                :page-size="pageSizePort"
                layout="total, sizes, prev, pager, next, jumper"
                :total="totalPort">
            </el-pagination>
          </div>
        </el-tab-pane>
      </el-tabs>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="编辑主机信息" :visible.sync="dialogFormVisible1" width="30%" :close-on-click-modal="false">
      <el-form label-width="100px" size="small" style="width: 90%">
        <el-form-item label="主机名称">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="主机flag码">
          <el-input v-model="form.flag" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="分数">
          <el-input v-model="form.grade" type="number" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="靶机组内编号">
          <el-input v-model="form.number" type="number" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="靶机类型">
          <el-select v-model="form.type" placeholder="请选择主机类型">
            <el-option label="用户可见" value="0"></el-option>
            <el-option label="用户不可见" value="1"></el-option>
          </el-select>
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
  name: "RangceHost",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      form: {},
      name:"",
      imageData:[],
      imageName:"",
      totalImage: 0,
      pageNumImage: 1,
      pageSizeImage: 2,
      nameImage:"",
      flavorData: [],
      totalFlavor: 0,
      pageNumFlavor: 1,
      pageSizeFlavor: 2,
      nameFlavor:"",
      flavorName:"",
      portData: [],
      totalPort: 0,
      pageNumPort: 1,
      pageSizePort: 2,
      namePort:"",
      portName:"",
      status:"DOWN",
      ipaddr:"",
      dialogFormVisible: false,
      dialogFormVisible1: false,
      multipleSelection: [],
      groupId:this.$route.query.groupId,
      rangceId:this.$route.query.rangceId,
      user: Cookies.get('cookie')? JSON.parse(Cookies.get('cookie')):{},
    }
  },
  created() {
   this.loadAll()
  },
  methods: {
    loadAll(){
      this.load()
      this.loadImage()
      this.loadFlavor()
      this.loadPort()
      this.timeGet()
    },

    load() {
      this.request.get("/host/page2", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
          rangceId:this.rangceId,
          groupId:this.groupId
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    save() {
      this.form.groupId=this.groupId
      this.form.rangceId=this.rangceId
      this.dialogFormVisible = false
      this.request.post("/host/save", this.form).then(res => {
        if (res.code === '200') {
          this.loadAll()
          this.dialogFormVisible1 = false;
          if(res.data!==null){
            this.$store.state.task.push({
              id: res.data.id,
              status:res.data.status
            })
          }
          this.$message.success(res.msg)
          this.loadAll()
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    del(id) {
      this.request.delete("/host/del/" + id).then(res => {
        if (res.code === '200') {
          this.$message.success(res.msg)
          this.loadAll()
        } else {
          this.$message.error(res.msg)
        }
      })
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





    handleAdd() {
      this.dialogFormVisible = true
      this.form={}
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.form.type=String(JSON.parse(JSON.stringify(row)).type)
      this.dialogFormVisible1 = true
    },
    selectImage(row) {
      this.form.imageId=row.id
      this.imageName=row.name
    },
    resetImage() {
      this.nameImage = ""
      this.loadImage()
    },
    handleSizeChangeImage(pageSizeImage) {
      this.pageSizeImage= pageSizeImage
      this.loadImage()
    },
    handleCurrentChangeImage(pageNumImage) {
      this.pageNumImage = pageNumImage
      this.loadImage()
    },
    loadImage() {
      this.request.get("/img/page", {
        params: {
          pageNum: this.pageNumImage,
          pageSize: this.pageSizeImage,
          name: this.nameImage,
          status: "ACTIVE"
        }
      }).then(res => {
        this.imageData = res.data.records
        this.totalImage = res.data.total
      })
    },


    selectPort(row) {
      this.form.portId=row.id
      this.portName=row.name
    },
    resetPort() {
      this.ipaddr = ""
      this.loadPort()
    },
    handleSizeChangePort(pageSizePort) {
      this.pageSizePort= pageSizePort
      this.loadPort()
    },
    handleCurrentChangePort(pageNumPort) {
      this.pageNumPort = pageNumPort
      this.loadPort()
    },
    loadPort() {
      this.request.get("/port/page", {
        params: {
          pageNum: this.pageNumPort,
          pageSize: this.pageSizePort,
          status:this.status,
          ipaddr:this.ipaddr
        }
      }).then(res => {
        this.portData = res.data.records
        this.totalPort = res.data.total
      })
    },

    selectFlavor(row) {
      this.form.flavorId=row.id
      this.flavorName=row.name
    },
    resetFlavor() {
      this.nameFlavor = ""
      this.loadFlavor()
    },
    handleSizeChangeFlavor(pageSizeFlavor) {
      this.pageSizeFlavor= pageSizeFlavor
      this.loadFlavor()
    },
    handleCurrentChangeFlavor(pageNumFlavor) {
      this.pageNumFlavor = pageNumFlavor
      this.loadFlavor()
    },
    loadFlavor() {
      this.request.get("/flavor/page", {
        params: {
          pageNum: this.pageNumFlavor,
          pageSize: this.pageSizeFlavor,
          name: this.nameFlavor,
        }
      }).then(res => {
        this.flavorData = res.data.records
        this.totalFlavor = res.data.total
      })
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
              this.loadAll()
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
          console.log(this.task)
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
          console.log(this.task)
          this.$message.success(res.msg)
          this.load()
        } else {
          this.$message.error(res.msg)
        }
      })

    }



  }
}
</script>

<style scoped>

</style>