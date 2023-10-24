<template>
  <div>
    <el-row :gutter="10" style="margin-bottom: 60px">
      <el-col :span="6">
        <el-card style="color: #409EFF">
          <div><i class="iconfont icon-yonghu" /> 用户总数</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">
            {{userNumber}}
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card style="color: #6cf577">
          <div><i class="iconfont icon-targetbazi" />在线人数</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">
            {{onlineNumber}}
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card style="color: #F56C6C">
          <div><i class="iconfont icon-targetbazi" /> 靶场总数</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">
           {{rangceNumber}}
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card style="color: #d76cf5">
          <div><i class="iconfont icon-server-host-full" /> 主机总数</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">
            {{hostNumber}}
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="8">
        <div ref="cpu" style="margin-left:-50px;width: 500px; height: 500px"></div>
      </el-col>
      <el-col :span="8">
        <div ref="ram" style="margin-left:-50px;width: 500px; height: 500px"></div>
      </el-col>
      <el-col :span="8">
        <div ref="disk" style="margin-left:-50px;width: 500px; height: 500px"></div>
      </el-col>
    </el-row>


  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: "Dashboard",
  data() {
    return {
      userNumber:"",
      rangceNumber:"",
      onlineNumber:"",
      hostNumber:"",
      cpuUseNumber:null,
      cpuAllNumber:null,
      ramUseNumber:null,
      ramAllNumber:null,
      diskUseNumber:null,
      diskAllNumber:null,
    }
  },
  created() {
    this.load()
  },

  mounted() {  // 页面元素渲染之后再触发
   this.loadUseNumber()
   this.loadAllNumber()

  },
  methods:{
    load() {
      this.request.get("/rangce/getNumber" ).then(res => {
        this.rangceNumber = res.data
      })
      this.request.get("/user/getNumber" ).then(res => {
        this.userNumber = res.data
      })
      this.request.get("/user/getOnlineNumber" ).then(res => {
        this.onlineNumber = res.data
      })
      this.request.get("/host/getNumber" ).then(res => {
        this.hostNumber = res.data
      })
    },
    loadAllNumber(){
      this.request.get("/dashboard/getOne" ).then(res => {
        this.diskAllNumber=res.data.disk
        this.cpuAllNumber=res.data.cpu
        this.ramAllNumber=res.data.ram
        this.myEcharts()
      })
    },
    loadUseNumber(){
      this.request.get("/dashboard/getUseNumber" ).then(res => {
        this.diskUseNumber=res.data.disk
        this.cpuUseNumber=res.data.cpu
        this.ramUseNumber=res.data.ram
      })
    },
    myEcharts(){
      const cpuChart = echarts.init(this.$refs.cpu);
      let cpuOption = {
        title: {
          text: 'CPU利用情况',
          left: 'center'
        },
        tooltip: {
          trigger: 'item'
        },
        legend: {
          top: '15%',
          left: 'center',
          // doesn't perfectly work with our tricks, disable it
          selectedMode: true
        },
        series: [
          {
            type: 'pie',
            radius: ['40%', '70%'],
            center: ['50%', '70%'],
            // adjust the start angle
            startAngle: 180,
            label: {
              show: false,
              formatter(param) {
                // correct the percentage
                return param.name ;
              }
            },
            data: [
              { value: this.cpuUseNumber, name: '已利用cpu/个',itemStyle:{color:"#76d5f3"} },
              { value:this.cpuAllNumber-this.cpuUseNumber,name:'未利用cpu/个',itemStyle:{color: 'gray'} },
              {
                value: this.cpuAllNumber,
                itemStyle: {
                  // stop the chart from rendering this piece
                  color: 'none',
                  decal: {
                    symbol: 'none'
                  }
                },
                label: {
                  show:false
                }
              }
            ]
          }
        ]
      };
      cpuChart.setOption(cpuOption);


      const ramChart = echarts.init(this.$refs.ram);
      let ramOption;
      ramOption = {
        title: {
          text: '内存利用情况',
          left: 'center'
        },
        tooltip: {
          trigger: 'item'
        },
        legend: {
          top: '15%',
          left: 'center',
          // doesn't perfectly work with our tricks, disable it
          selectedMode: true
        },
        series: [
          {
            type: 'pie',
            radius: ['40%', '70%'],
            center: ['50%', '70%'],
            // adjust the start angle
            startAngle: 180,
            label: {
              show: false,
              formatter(param) {
                // correct the percentage
                return param.name ;
              }
            },
            data: [
              { value: this.ramUseNumber/1024, name: '已利用ram/GB',itemStyle:{color:"#7cefde"} },
              { value:(this.ramAllNumber-this.ramUseNumber)/1024,name:'未利用ram/GB',itemStyle:{color: 'gray'} },

              {
                value: this.ramAllNumber/1024,
                itemStyle: {
                  // stop the chart from rendering this piece
                  color: 'none',
                  decal: {
                    symbol: 'none'
                  }
                },
                label: {
                  show: false
                }
              }
            ]
          }
        ]
      };
      ramChart.setOption(ramOption);


      let diskChart = echarts.init(this.$refs.disk);
      let diskOption = {
        title: {
          text: '磁盘利用情况',
          left: 'center'
        },
        tooltip: {
          trigger: 'item'
        },
        legend: {
          top: '15%',
          left: 'center',
          // doesn't perfectly work with our tricks, disable it
          selectedMode: true
        },
        series: [
          {
            type: 'pie',
            radius: ['40%', '70%'],
            center: ['50%', '70%'],
            // adjust the start angle
            startAngle: 180,
            label: {
              show: false,
              formatter(param) {
                // correct the percentage
                return param.name ;
              }
            },
            data: [
              { value: this.diskUseNumber, name: '已利用磁盘/GB',itemStyle:{color:"#7cef97"} },
              { value:this.diskAllNumber-this.diskUseNumber,name:'未利用磁盘/GB',itemStyle:{color: 'gray'} },

              {
                value: this.diskAllNumber,
                itemStyle: {
                  // stop the chart from rendering this piece
                  color: 'none',
                  decal: {
                    symbol: 'none'
                  }
                },
                label: {
                  show: false
                }
              }
            ]
          }
        ]
      };
      diskChart.setOption(diskOption);
    }
  },

}
</script>

<style scoped>

</style>
