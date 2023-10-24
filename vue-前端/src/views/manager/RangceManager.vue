<template>
  <div>
    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="name"></el-input>
      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
      <el-button type="primary" @click="handleAdd">新增 <i class="el-icon-circle-plus-outline"></i></el-button>
    </div>


    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"  >
      <el-table-column prop="id" label="ID" width="80" sortable></el-table-column>
      <el-table-column prop="name" label="靶场名称">
        <template v-slot="scope">
          <span @click="handleRouter2(scope.row)" style="color: #409EFF">{{ scope.row.name}}</span>
        </template>
      </el-table-column>
      <el-table-column prop="time" label="发布时间"></el-table-column>
      <el-table-column prop="anumber" label="靶场总数"></el-table-column>
      <el-table-column prop="grade" label="靶场分数"></el-table-column>
      <el-table-column label="启用">
        <template slot-scope="scope">
          <el-switch
              v-model="scope.row.status"
              active-color="#13ce66"
              inactive-color="#ff4949"
              @change="changeStatus(scope.row)"
          >
          </el-switch>
        </template>
      </el-table-column>
      <el-table-column label="封面"><template v-slot="scope"><el-image style="width: 40px; height: 40px" :src="scope.row.url" :preview-src-list="[scope.row.url]"></el-image></template></el-table-column>
      <el-table-column prop="introduction" label="靶场介绍">
        <template v-slot="scope">
          <el-popover trigger="hover" placement="top">
            <div v-html="scope.row.introduction"></div>
            <div slot="reference" class="name-wrapper">
              <el-tag size="medium">靶场介绍</el-tag>
            </div>
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column label="操作"  width="300" align="center">
        <template v-slot="scope">
          <el-button type="success" @click="handleEdit(scope.row)">编辑<i class="el-icon-edit"></i></el-button>
          <el-button type="primary" @click="handleRouter(scope.row)">靶机组管理</el-button>
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
    <el-dialog title="靶场信息设置" :visible.sync="dialogFormVisible" width="60%" :close-on-click-modal="false">
      <el-form label-width="100px" size="small" style="width: 90%">
        <el-form-item label="靶场名称">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="CIDR">
          <el-input v-model="form.cidr"type="cidr" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="使用时长">
          <el-input  v-model="form.useTime"type="time" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="封面">
          <el-upload
              class="avatar-uploader"
              action="http://localhost:8081/file/upload"
              ref="img"
              :show-file-list="false"
              :on-success="handleImgUploadSuccess">
            <img v-if="form.url" :src="form.url" class="avatar" alt="封面">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
        <el-form-item label="介绍">
          <div id="richText"></div>
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
import E from "wangeditor"
import Cookies from "js-cookie";
let editor;
export default {
  name: "RangceManager",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      form: {},
      name:"",
      dialogFormVisible: false,
      content: '',
      multipleSelection: [],
      user: Cookies.get('cookie')? JSON.parse(Cookies.get('cookie')):{},
    }
  },
  created() {
    this.load()
  },
  methods: {
    view(content) {
      this.content = content
      this.dialogFormVisible1 = true
    },
    load() {
      this.request.get("/rangce/page", {
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
      const content = editor.txt.html()
      // 注意：这个地方需要手动赋值
      this.form.introduction = content
      this.request.post("/rangce/save", this.form).then(res => {
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
      this.request.delete("/rangce/del/" + id).then(res => {
        if (res.code === '200') {
          this.$message.success("删除成功")
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

      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.load()
    },
    handleAdd() {
      this.dialogFormVisible = true
      this.form = {url: ''}
      this.$nextTick(() => {
        if(!editor) {
          editor = new E("#richText")
          editor.config.uploadImgServer = 'http://localhost:8081/file/upload/editor'
          editor.config.uploadFileName = 'file'
          editor.create()
        }
        editor.txt.html('')  // 清除内容

        if(this.$refs.img) {
          this.$refs.img.clearFiles();
        }
        if(this.$refs.file) {
          this.$refs.file.clearFiles();
        }
      })
    },
    handleRouter(row) {
      this.$router.push({
        path: '/Ahome/rangce_group',
        query: {
          id: row.id,
        }
      })

    },
    handleRouter2(row) {
      this.$router.push({
        path: '/Ahome/rangce_detail',
        query: {
          id: row.id,
          form: row,
        }
      })

    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible = true
      this.$nextTick(() => {
        if(!editor) {
          editor = new E("#richText")
          editor.config.uploadImgServer = 'http://localhost:8081/file/upload/editor'
          editor.config.uploadFileName = 'file'
          editor.create()
        }
        editor.txt.html(this.form.introduction)
        if(this.$refs.img) {
          this.$refs.img.clearFiles();
        }
        if(this.$refs.file) {
          this.$refs.file.clearFiles();
        }
      })
    },
    handleImgUploadSuccess(res) {
     this.form.url = res.data
    },
    changeStatus(row) {
      this.request.post("/rangce/save", row).then(res => {
        if (res.code === '200') {
          this.$message.success("操作成功")
        }
      })
    },



  }
}
</script>

<style  scoped lang="less">
.headerBg {
  background: #eee!important;
}
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
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