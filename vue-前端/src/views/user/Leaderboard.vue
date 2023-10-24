<template>
  <div class="Leaderboard">
    <div class="title">
      <span style="font-size: 50px"><b>积分榜</b></span>
    </div>
    <div class="infinite-list-wrapper">
      <ul class="list" v-infinite-scroll="load">
        <li v-for="item in leaderboard" class="list-item">
          <el-card class="card">
            <span class="index"><b>{{ index++ }}</b></span>
            <span class="nickname"><b>{{item.nickname}}</b></span>
            <span class="grade"><b>{{item.grade}}</b></span>
          </el-card>
        </li>
      </ul>
    </div>
  </div>

</template>

<script>
export default {
  name: "Leaderboard",
  data(){
    return{
      index:1,
      leaderboard:[],
    }
  },
  created() {
    this.load()
  },
  methods:{
    load() {
      this.request.get("/user/grade").then(res => {
        this.leaderboard=res.data
      })
    },
  }
}
</script>

<style scoped  lang="less">
.Leaderboard{
  position: relative;
  width: 70%;
  margin: 0 auto;
  .title{
    width:20%;
    top:5%;
    margin: 0 auto;
  }
  .infinite-list-wrapper{
    width: 90%;
    margin: 0 auto;
    overflow:auto;
    .list{
     .card{
       position: relative;
       .index{
         margin-left: 5%;
       }
       .nickname{
         position:absolute;
         width:20%;
         margin-left: 35%;
         font-size: 20px;
       }
       .grade{
         position:absolute;
         width:20%;
         margin-left: 75%;
         font-size: 20px;
       }
     }
    }
  }
}

</style>