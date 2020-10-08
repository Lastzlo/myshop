import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        onCatalogDrawer: false,
        onAutorizationDrawer: false,
    },
    getters:{
        // catalogDrawer: state => state.onCatalogDrawer,
        // autorizationDrawer: state => state.onAutorizationDrawer
    },
    mutations: {
        changeAutorizationDrawerMutation(state){
            state.onAutorizationDrawer = !state.onAutorizationDrawer
        },
        changeCatalogDrawerMutation(state){
            state.onCatalogDrawer = !state.onCatalogDrawer
        }
    },
})