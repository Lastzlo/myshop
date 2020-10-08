import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        count: 0,
        onCatalogDrawer: false,
        onAutorizationDrawer: false,
    },
    getters:{
        catalogDrawer: state => state.onCatalogDrawer,
        autorizationDrawer: state => state.onAutorizationDrawer
    },
    mutations: {
        increment (state) {
            state.count++
        },
        changeAutorizationDrawerMutation(state){
            const oldStatus = state.onAutorizationDrawer
            state.onAutorizationDrawer = !oldStatus

        }
    },
    // actions:{
    //     async changeAutorizationDrawerAction({commit}){
    //         commit('changeAutorizationDrawerMutation')
    //     }
    // }
})