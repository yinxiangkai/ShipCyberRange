import Vue from 'vue'
import Vuex from 'vuex'
import router from "@/router";
import Cookies from "js-cookie";

Vue.use(Vuex)

const store = new Vuex.Store({
    state: {
        currentPathName: '',
        task:[]
    },
    mutations: {
        setPath (state) {
            state.currentPathName = localStorage.getItem("currentPathName")
        },
        logout() {

            // 清空缓存
            localStorage.removeItem("cookie")
            Cookies.remove('cookie')
            router.push("/")

        }
    }
})

export default store
