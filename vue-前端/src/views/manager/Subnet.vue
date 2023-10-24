<template>
  <div>
    <el-tabs type="border-card">
      <el-tab-pane :label="subnetName">
        <div style="margin: 10px 0">
          <el-button type="primary" @click="handleAdd" >新增子网<i class="el-icon-circle-plus-outline"></i></el-button>

        </div>

        <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"  >
          <el-table-column prop="id" label="ID" width="80" sortable></el-table-column>
          <el-table-column prop="name" label="子网名称"></el-table-column>
          <el-table-column prop="cidr" label="CIDR"></el-table-column>
          <el-table-column prop="gateway" label="网关"></el-table-column>
          <el-table-column prop="version" label="IP版本"></el-table-column>

          <el-table-column label="操作"  width="220" align="center">
            <template v-slot="scope">
              <el-button type="success" @click="handleEdit(scope.row)">创建端口<i class="el-icon-circle-plus-outline"></i></el-button>
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
        <el-dialog title="创建子网" :visible.sync="dialogFormVisible" width="30%" :close-on-click-modal="false">
          <el-form label-width="100px" size="small" style="width: 90%">
            <el-form-item label="子网名称">
              <el-input v-model="form.name" autocomplete="off" style="width: 200px"></el-input>
            </el-form-item>
            <el-form-item label="CIDR">
              <el-input v-model="form.cidr" autocomplete="off" style="width: 200px"></el-input>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="dialogFormVisible = false">取 消</el-button>
            <el-button type="primary" @click="save">确 定</el-button>
          </div>
        </el-dialog>
        <el-dialog title="创建端口" :visible.sync="dialogFormVisible1" width="30%" :close-on-click-modal="false">
          <el-form label-width="100px" size="small" style="width: 90%">
            <el-form-item label="端口名称">
              <el-input v-model="formPort.name" autocomplete="off" style="width: 200px"></el-input>
            </el-form-item>
            <el-form-item label="IP地址">
              <el-input v-model="formPort.ipaddr" autocomplete="off" style="width: 200px"></el-input>
            </el-form-item>

          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="dialogFormVisible1 = false">取 消</el-button>
            <el-button type="primary" @click="savePort">确 定</el-button>
          </div>
        </el-dialog>
      </el-tab-pane>
      <el-tab-pane :label="portName">
        <el-input style="width: 200px" placeholder="请输入IP" suffix-icon="el-icon-search" v-model="ipaddr"></el-input>
        <el-button class="ml-5" type="primary" @click="loadPort">搜索</el-button>
        <el-button type="warning" @click="resetPort">重置</el-button>
        <el-table :data="tableDataPort" border stripe :header-cell-class-name="'headerBg'"  >
          <el-table-column prop="id" label="ID" width="80" sortable></el-table-column>
          <el-table-column prop="name" label="端口名称"></el-table-column>
          <el-table-column prop="ipaddr" label="IP地址"></el-table-column>
          <el-table-column prop="subnet" label="所属子网"></el-table-column>
          <el-table-column prop="status" label="状态"></el-table-column>

          <el-table-column label="操作"  width="180" align="center">
            <template v-slot="scope">
              <el-popconfirm
                  class="ml-5"
                  confirm-button-text='确定'
                  cancel-button-text='我再想想'
                  icon="el-icon-info"
                  icon-color="red"
                  title="您确定删除吗？"
                  @confirm="delPort(scope.row.id)"
              >
                <el-button type="danger" slot="reference">删除 <i class="el-icon-remove-outline"></i></el-button>
              </el-popconfirm>
            </template>
          </el-table-column>
        </el-table>
        <div style="padding: 10px 0">
          <el-pagination
              @size-change="handleSizeChangePort"
              @current-change="handleCurrentChangePort"
              :current-page="pageNumPort"
              :page-sizes="[2, 5, 10, 20]"
              :page-size="pageSizePort"
              layout="total, sizes, prev, pager, next, jumper"
              :total="totalPort">
          </el-pagination>
        </div>

      </el-tab-pane>
    </el-tabs>

  </div>
</template>

<script>
import Cookies from "js-cookie";

export default {
  name: "Subnet",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      form: {},
      tableDataPort: [],
      totalPort: 0,
      pageNumPort: 1,
      pageSizePort: 10,
      formPort: {},
      network:0,
      status:"",
      subnetName:"",
      portName:"",
      ipaddr:"",
      dialogFormVisible: false,
      dialogFormVisible1: false,
      fullscreenLoading: false,
      multipleSelection: [],
      user: Cookies.get('cookie')? JSON.parse(Cookies.get('cookie')):{},
    }
  },
  created() {
    this.subnetName=this.$route.query.name+"子网管理"
    this.portName=this.$route.query.name+"端口管理"
    this.network=this.$route.query.id
    this.load()
    this.loadPort()


  },
  methods: {

    load() {
      this.request.get("/subnet/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          networkid: this.network,
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    loadPort() {
      this.request.get("/port/page", {
        params: {
          pageNum: this.pageNumPort,
          pageSize: this.pageSizePort,
          networkid: this.network,
          status:this.status,
          ipaddr:this.ipaddr
        }
      }).then(res => {
        this.tableDataPort = res.data.records
        this.totalPort = res.data.total
      })
    },
    save() {
      this.form.networkid=this.network
      this.fullscreenLoading = true;
      this.request.post("/subnet/save", this.form).then(res => {
        if (res.code === '200') {
          this.fullscreenLoading = false;
          this.$message.success(res.msg)
          this.dialogFormVisible = false
          this.load()
        } else {
          this.fullscreenLoading = false;
          this.$message.error(res.msg)
        }
      })
    },
    savePort() {
      this.formPort.networkId=this.network
      this.dialogFormVisible1 = false
      this.request.post("/port/save", this.formPort).then(res => {
        if (res.code === '200') {
          this.$message.success(res.msg)
          this.loadPort()
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    delPort(id) {
      this.request.delete("/port/del/" + id).then(res => {
        if (res.code === '200') {
          this.$message.success(res.msg)
          this.loadPort()
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    del(id) {
      this.request.delete("/subnet/del/" + id).then(res => {
        if (res.code === '200') {
          this.$message.success(res.msg)
          this.load()
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    handleSizeChangePort(pageSize) {
      this.pageSizePort = pageSize
      this.loadPort()
    },
    handleCurrentChangePort(pageNum) {
      this.pageNumPort = pageNum
      this.loadPort()
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
      this.form={},
      this.dialogFormVisible = true
    },
    handleEdit(row) {
      this.formPort={},
      this.formPort.subnetId = row.id
      this.dialogFormVisible1 = true
    },
    resetPort(){
      this.ipaddr=""
      this.loadPort()
    }


  }
}
</script>

<style scoped>

</style>