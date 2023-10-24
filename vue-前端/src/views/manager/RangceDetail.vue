<template>
  <div style="position: relative;">



    <el-tabs v-model="activeName">
      <el-tab-pane label="靶场设备设置" name="first">
        <div style="margin: 10px 0">
          <el-button type="primary" @click="handleAdd">新增 <i class="el-icon-circle-plus-outline"></i></el-button>
        </div>

        <el-table :data="tableData"  border stripe :header-cell-class-name="'headerBg'"  >
          <el-table-column prop="id" label="ID" width="80" sortable></el-table-column>
          <el-table-column prop="name" label="设备名称"></el-table-column>
          <el-table-column prop="number" label="设备编号"></el-table-column>
          <el-table-column prop="ipaddr" label="设备IP"></el-table-column>
          <el-table-column prop="type" label="设备类型">
            <template v-slot="scope">
              <el-tag size="medium"  type="success" v-if="scope.row.type===0">网关</el-tag>
              <el-tag size="medium"  type="primary" v-if="scope.row.type===1">主机</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="type" label="用户可见性">
            <template v-slot="scope">
              <el-tag size="medium"  type="success" v-if="scope.row.status">用户可见</el-tag>
              <el-tag size="medium"  type="warning" v-else="scope.row.status">用户不可见</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="图片">
            <template v-slot="scope">
              <el-image style="width: 100px; height: 100px" :src="scope.row.imageUrl" :preview-src-list="[scope.row.imageUrl]"></el-image>
            </template>
          </el-table-column>
          <el-table-column label="操作"  width="240" align="center">
            <template v-slot="scope">
              <el-button type="success" @click="handleEdit(scope.row)">编辑 <i class="el-icon-edit"></i></el-button>
              <el-popconfirm
                  class="ml-5"
                  confirm-button-text='确定'
                  cancel-button-text='我再想想'
                  icon="el-icon-info"
                  icon-color="red"
                  title="您确定删除吗？"
                  @confirm="del1(scope.row.id)"
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


        <el-dialog title="信息" :visible.sync="dialogFormVisible1" width="80%" :modal="true">
          <el-tabs tab-position="left">
            <el-tab-pane label="信息设置">
              <el-form label-width="100px" size="small" style="width: 90%">
                <el-form-item label="图标">
                  <el-upload
                      class="avatar-uploader"
                      action="http://localhost:8081/file/upload"
                      :show-file-list="false"
                      :on-success="handleImgUploadSuccess"
                  >
                    <img v-if="imgB" :src="form.imageUrl" class="avatar" alt="图标">
                    <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                  </el-upload>
                </el-form-item>
                <el-form-item label="设备名称">
                  <el-input   v-model="form.name" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="设备编号">
                  <el-input  type="number" v-model="form.number" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="设备类型">
                  <el-select v-model="form.type" placeholder="请选择编号类型">
                    <el-option label="网关" value="0"></el-option>
                    <el-option label="主机" value="1"></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item v-if="form.type==='1'" label="前置设备编号">
                  <el-input  type="Number" v-model="form.preNumber" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item  label="设备ip">
                  <el-input   v-model="form.ipaddr" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item v-if="form.type==='1'" label="分数">
                  <el-input v-model="form.grade" type="number" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item v-if="form.type==='1'" label="flag码">
                  <el-input v-model="form.flag" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="靶机类型">
                  <el-select v-model="form.status" placeholder="用户可见性">
                    <el-option label="用户可见" value="true"></el-option>
                    <el-option label="用户不可见" value="false"></el-option>
                  </el-select>
                </el-form-item>
              </el-form>
            </el-tab-pane>
            <el-tab-pane label="镜像设置"  v-if="form.type==='1'">
              <div style="margin: 10px 0">
                <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="nameImage"></el-input>
                <el-button class="ml-5" type="primary" @click="loadImage">搜索</el-button>
                <el-button type="warning" @click="resetImage">重置</el-button>
                <span style="margin-left: 40px;">当前选择的镜像:<span style="color: #757474;font-size: 20px">{{this.imageName}}</span></span>
              </div>
              <div class="table" >
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
            <el-tab-pane label="规格设置" v-if="form.type==='1'">
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

        </el-tabs>
          <div slot="footer" class="dialog-footer">
            <el-button @click="dialogFormVisible1 = false">取 消</el-button>
            <el-button type="primary" @click="save1">确 定</el-button>
          </div>
        </el-dialog>
      </el-tab-pane>
      <el-tab-pane label="信息展示" name="second">
        <el-card style="position: relative;height:440px;margin: 0 auto">
        <div class="picture">
          <img :src="rangce.url" style="width:100%; height: 100%" >
        </div>
        <div class="content">
          <div>
            <b style="color: #3c3f41;font-size: 20px;margin-bottom: 5px">靶场名称：{{rangce.name}}</b>
          </div>
          <div>
            <b style="color: #3c3f41;font-size: 20px;margin-bottom: 5px">靶场介绍：</b>
          </div>
          <div v-html="rangce.introduction" style="margin-left: 90px; "></div>
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
                    <el-button type="text" style="color: red" @click="del(item.id)" ><b style="color: #e63a54;font-size: 14px">删除</b></el-button>
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
                      <el-button type="text"  @click="del(subItem.id)"><b style="color: #e63a54;font-size: 14px">删除</b></el-button>
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
      </el-tab-pane>
      <el-tab-pane label="控制台展示设置" name="third" >
          <div style="width: 657px;height: 300px; border:2px solid #479de1; position: relative;left: 20%" @click="getPosition">
            <el-tag size="medium"  type="success"v-for="(item,index) in tableData" :key="index" class="alert" :style="{left: item.positionX + 'px', top: item.positionY+ 'px'}">
              {{item.name}}
            </el-tag>
            <img :src="rangce.background" style="width:100%; height: 100%" >
          </div>
        <div style="position: absolute;top: 20%;right: 10%">
          <el-upload
            class="avatar-uploader"
            action="http://localhost:8081/file/upload"
            ref="img"
            :show-file-list="false"
            :on-success="handleImgRangceSuccess"
            style=""
        >
          <i  class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload></div>


            <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"   >
              <el-table-column prop="id" label="ID" width="80" sortable></el-table-column>
              <el-table-column prop="name" label="设备名称"></el-table-column>
              <el-table-column prop="number" label="设备编号"></el-table-column>
              <el-table-column prop="positionX" label="坐标X"></el-table-column>
              <el-table-column prop="positionY" label="坐标Y"></el-table-column>
              <el-table-column label="操作"  width="340" align="center">
                <template v-slot="scope">
                  <el-button type="success"  @click="setPosition(scope.row)">设置坐标 <i class="el-icon-edit"></i></el-button>
                  <el-button type="warning"  @click="noPosition()">取消设置模式 <i class="el-icon-edit"></i></el-button>
                </template>
              </el-table-column>
            </el-table>
      </el-tab-pane>

    </el-tabs>



  </div>
</template>

<script>
import Cookies from "js-cookie";
import {left} from "core-js/internals/array-reduce";
export default {
  name: "RangceDetail",
  data() {
    return {
      activeName:'second',
      tableData: [],
      total: 0,
      form:{},
      pageNum: 1,
      pageSize: 10,
      role:false,
      comments: [],
      rangce:{},
      commentForm: {},
      flagForm:{},
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
      imgB:true,
      ifPosition:false,
      current:'',
      rangceId:this.$route.query.id,
      dialogFormVisible: false,
      dialogFormVisible1: false,
      user: Cookies.get('cookie')? JSON.parse(Cookies.get('cookie')):{},
    }
  },
  created() {
    this.load()
    this.loadComment()
    this.getRole()
    this.loadVis()
  },
  methods: {
    left,
    view(content) {
      this.content = content
      this.dialogFormVisible1 = true
    },
    load() {
      this.request.get("/rangce/" + this.rangceId).then(res => {
        this.rangce = res.data
      })
      this.loadImage()
      this.loadFlavor()
    },
    loadVis() {
      this.request.get("/rangce-host/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          rangceId:this.rangceId
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
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
    handleImgRangceSuccess(res) {
      this.rangce.background = res.data
      this.request.post("/rangce/save", this.rangce).then(res => {
        if (res.code === '200') {
          this.$message.success(res.msg)
          this.dialogFormVisible = false
          this.load()
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
          this.role=res.data
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
          this.loadVis()
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    save1() {
      this.form.rangceId=this.rangceId
      this.request.post("/rangce-host/save", this.form).then(res => {
        if (res.code === '200') {
          this.$message.success(res.msg)
          this.dialogFormVisible1 = false
          this.loadVis()
        } else {
          this.$message.error(res.msg)
          this.dialogFormVisible1 = false
        }
      })
    },
    save2() {
      this.form.rangceId=this.rangceId
      this.request.post("/rangce-host/save", this.form).then(res => {
        if (res.code === '200') {
          this.loadVis()
        } else {
        }
      })
    },
    del1(id) {
      this.request.delete("/rangce-host/del/" + id).then(res => {
        if (res.code === '200') {
          this.$message.success(res.msg)
          this.loadVis()
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    handleAdd() {
      this.imgB=false
      this.form={}
      this.dialogFormVisible1 = true
    },
    handleImgUploadSuccess(res) {
      this.imgB=true
      this.form.imageUrl = res.data

    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.loadVis()
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.loadVis()
    },
    handleEdit(row) {
      this.imgB=true
      this.form = JSON.parse(JSON.stringify(row))
      this.form.type=String(JSON.parse(JSON.stringify(row)).type)
      this.form.status=String(JSON.parse(JSON.stringify(row)).status)
      this.dialogFormVisible1 = true
    },
    setPosition(row) {
      this.ifPosition=true
      this.current=row.id
      this.form = JSON.parse(JSON.stringify(row))
    },
    noPosition() {
      this.current=''
      this.ifPosition=false
      this.form ={}


    },
    getPosition:function(event){
      if(this.ifPosition){
        this.form.positionX=event.offsetX;
        this.form.positionY=event.offsetY;
        this.save2();
      }

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
  overflow: auto;
  position: absolute;
  height:80%;
  width:55%;
  top:10%;
  left:40%;
}
.avatar-uploader {
  text-align: center;
  padding-bottom: 10px;

}
/deep/.avatar-uploader  .el-upload   {
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
.alert{
  /*这个样式不写，右键弹框会一直显示在画布的左下角*/
  position: absolute;
  opacity: 0.8;
  font-size: 10px;
  border-radius: 5px;
  z-index: 10;
  cursor: move;
}
</style>