import Vue from 'vue'
import VueRouter from 'vue-router'
import store from "@/store";
import Cookies from 'js-cookie'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: '登录',
    component: () => import('../views/system/Login.vue')
  },
  {
    path: '/404',
    name: '404',
    component: () => import('../views/system/404.vue')
  },
  {
    path: '/Shome',
    name: '超级管理员首页',
    component: () => import('../views/manager/Shome.vue'),

  },
  {
    path: '/Ahome',
    name: '管理员首页',
    component: () => import('../views/manager/Ahome.vue'),
    redirect:'/Ahome/Dashboard',
    children: [
      { path: '/Ahome/user_manager', name: '用户管理', component: () => import('../views/manager/UserManager.vue')},
      { path: '/Ahome/img_manager', name: '镜像管理', component: () => import('../views/manager/ImgManager.vue')},
      { path: '/Ahome/rangce_manager', name: '靶场管理', component: () => import('../views/manager/RangceManager.vue')},
      { path: '/Ahome/notice', name: '通知', component: () => import('../views/manager/Notice.vue')},
      { path: '/Ahome/webchat', name: '在线联系', component: () => import('@/views/manager/WebChat.vue')},
      { path: '/Ahome/flavor', name: '规格管理', component: () => import('@/views/manager/Flavor.vue')},
      { path: '/Ahome/network', name: '网络管理', component: () => import('@/views/manager/NetworkManager.vue')},
      { path: '/Ahome/subnet', name: '网络详情', component: () => import('@/views/manager/Subnet.vue')},
      { path: '/Ahome/rangce_group', name: '靶机组管理', component: () => import('@/views/manager/RangceGroup.vue')},
      { path: '/Ahome/rangce_host', name: '靶机组所属主机', component: () => import('@/views/manager/RangceHost.vue')},
      { path: '/Ahome/host_manager', name: '主机管理', component: () => import('@/views/manager/HostManager.vue')},
      { path: '/Ahome/cover', name: '主页轮播图管理', component: () => import('@/views/manager/CoverPage.vue')},
      { path: '/Ahome/host_detail', name: '主机详情', component: () => import('@/views/manager/HostDetail.vue')},
      { path: '/Ahome/rangce_detail', name: '靶场详情', component: () => import('@/views/manager/RangceDetail.vue')},
      { path: '/Ahome/user_rangce', name: '用户靶场管理', component: () => import('@/views/manager/UserRangce.vue')},
      { path: '/Ahome/Dashboard', name: '数据面板', component: () => import('@/views/manager/Dashboard.vue')},



    ]
  },
  {
    path: '/Uhome',
    name: '用户主页',
    redirect:'/Uhome/home',
    component: () => import('../views/user/Uhome.vue'),
    children: [
      { path: '/Uhome/home', name: '主页', component: () => import('../views/user/Home.vue')},
      { path: '/Uhome/webchat', name: '在线通讯', component: () => import('../views/user/WebChat.vue')},
      { path: '/Uhome/notify', name: '系统公告', component: () => import('../views/user/Notify.vue')},
      { path: '/Uhome/rangce_list', name: '靶场资源', component: () => import('../views/user/RangceList.vue')},
      { path: '/Uhome/rangce', name: '靶场信息', component: () => import('../views/user/Rangce.vue')},
      { path: '/Uhome/leaderboard', name: '排行榜', component: () => import('../views/user/Leaderboard.vue')},
      { path: '/Uhome/my_rangce', name: '我的靶场信息', component: () => import('../views/user/MyRangce.vue')},
      { path: '/Uhome/up_history', name: '提交记录', component: () => import('../views/user/UpHistory.vue')},
      { path: '/Uhome/rangce_large', name: '靶场使用显示', component: () => import('../views/user/RangceLarge.vue')},
    ]
  },
  {
    path: '*',
    redirect:'/404'
  },



]



const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})


// 路由守卫
router.beforeEach((to, from, next) => {
  localStorage.setItem("currentPathName", to.name)  // 设置当前的路由名称，为了在Header组件中去使用
  store.commit("setPath")  // 触发store的数据更新
  const cookie=Cookies.get('cookie')
  if (cookie) {

    if(JSON.parse(cookie).role!==0&&to.path==='/Shome'){
      alert('超级管理员未登录请重新登录')
      return next("/")
    }
    if(JSON.parse(cookie).role!==1 && to.path==='/Ahome'){
      alert('管理员未登录请重新登录')
      return next("/")
    }
    if(JSON.parse(cookie).role!==1&&to.path==='/Ahome/user_manager'){
      alert('管理员未登录请重新登录')
      return next("/")
    }
    if(JSON.parse(cookie).role!==1&&to.path==='/Ahome/img_manager'){
      alert('管理员未登录请重新登录')
      return next("/")
    }
    if(JSON.parse(cookie).role!==1&&to.path==='/Ahome/rangce_manager'){
      alert('管理员未登录请重新登录')
      return next("/")
    }
    if(JSON.parse(cookie).role!==1&&to.path==='/Ahome/notice'){
      alert('管理员未登录请重新登录')
      return next("/")
    }
    if(JSON.parse(cookie).role!==1&&to.path==='/Ahome/webchat'){
      alert('管理员未登录请重新登录')
      return next("/")
    }
    if(JSON.parse(cookie).role!==1&&to.path==='/Ahome/network'){
      alert('管理员未登录请重新登录')
      return next("/")
    }
    if(JSON.parse(cookie).role!==1&&to.path==='/Ahome/subnet'){
      alert('管理员未登录请重新登录')
      return next("/")
    }
    if(JSON.parse(cookie).role!==1&&to.path==='/Ahome/rangce_group'){
      alert('管理员未登录请重新登录')
      return next("/")
    }
    if(JSON.parse(cookie).role!==1&&to.path==='/Ahome/rangce_host'){
      alert('管理员未登录请重新登录')
      return next("/")
    }
    if(JSON.parse(cookie).role!==1&&to.path==='/Ahome/host_manager'){
      alert('管理员未登录请重新登录')
      return next("/")
    }
    if(JSON.parse(cookie).role!==1&&to.path==='/Ahome/cover'){
      alert('管理员未登录请重新登录')
      return next("/")
    }
    if(JSON.parse(cookie).role!==1&&to.path==='/Ahome/host_detail'){
      alert('管理员未登录请重新登录')
      return next("/")
    }
    if(JSON.parse(cookie).role!==1&&to.path==='/Ahome/rangce_detail'){
      alert('管理员未登录请重新登录')
      return next("/")
    }
    if(JSON.parse(cookie).role!==1&&to.path==='/Ahome/user_rangce'){
      alert('管理员未登录请重新登录')
      return next("/")
    }
    if(JSON.parse(cookie).role!==1&&to.path===' /Ahome/Dashboard'){
      alert('管理员未登录请重新登录')
      return next("/")
    }




    if(JSON.parse(cookie).role!==2&&to.path==='/Uhome'){
      alert('用户未登录请重新登录')
      return next("/")
    }
    if(JSON.parse(cookie).role!==2&&to.path==='/Uhome/webchat'){
      alert('用户未登录请重新登录')
      return next("/")
    }
    if(JSON.parse(cookie).role!==2&&to.path==='/Uhome/noyify'){
      alert('用户未登录请重新登录')
      return next("/")
    }
    if(JSON.parse(cookie).role!==2&&to.path==='/Uhome/flavor'){
      alert('用户未登录请重新登录')
      return next("/")
    }
    if(JSON.parse(cookie).role!==2&&to.path==='/Uhome/home'){
      alert('用户未登录请重新登录')
      return next("/")
    }
    if(JSON.parse(cookie).role!==2&&to.path==='/Uhome/rangce_list'){
      alert('用户未登录请重新登录')
      return next("/")
    }
    if(JSON.parse(cookie).role!==2&&to.path==='/Uhome/rangce'){
      alert('用户未登录请重新登录')
      return next("/")
    }
    if(JSON.parse(cookie).role!==2&&to.path==='/Uhome/leaderboard'){
      alert('用户未登录请重新登录')
      return next("/")
    }
    if(JSON.parse(cookie).role!==2&&to.path==='/Uhome/up_history'){
      alert('用户未登录请重新登录')
      return next("/")
    }
    if(JSON.parse(cookie).role!==2&&to.path==='/Uhome/my_rangce'){
      alert('用户未登录请重新登录')
      return next("/")
    }
    if(JSON.parse(cookie).role!==2&&to.path==='/Uhome/rangce_large'){
      alert('用户未登录请重新登录')
      return next("/")
    }

  //新路径

  }else if (to.path!=='/'){
    alert('未登录请登录')
    return next("/")
  }

  next()  // 放行路由
})

export default router
