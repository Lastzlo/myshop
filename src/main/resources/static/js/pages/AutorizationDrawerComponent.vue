<template>
    <v-navigation-drawer
            id="autorizationDrawer"
            v-model="onAutorization"
            absolute
            temporary
            right
    >
        <v-list-item>
            <v-list-item-content>
                <v-list-item-title class="title">
                    Авторизация
                </v-list-item-title>
            </v-list-item-content>
        </v-list-item>
        <v-divider></v-divider>

        <v-list
                nav
                dense
        >
            <v-list-item @click="openPage">
                <v-list-item-title>Войдти через Google</v-list-item-title>
            </v-list-item>

            <v-list-item @click="openPage">
                <v-list-item-title>Войдти через Facebook</v-list-item-title>
            </v-list-item>

            <v-list-item @click="openPage">
                <v-list-item-title>Регистрация</v-list-item-title>
            </v-list-item>

            <v-list-item @click="showProductList">
                <v-list-item-title>Страница управления товарами</v-list-item-title>
            </v-list-item>
        </v-list>
    </v-navigation-drawer>
</template>
<script>
    import {mapState} from 'vuex'


    export default {
        name: 'AutorizationDrawerComponent',
        props: ['changeOnAutorizationDrawer','onAutorizationDrawer'],
        data () {
            return {
                onAutorization: false
            }
        },
        // computed:
        //     mapState(['onAutorizationDrawer'])
        // ,
        watch: {
            // onAutorization: function (newVal, oldVal) {
            //     this.changeOnAutorizationDrawer(newVal)
            // },
            onAutorizationDrawer: function (newVal, oldVal) {
                this.onAutorization = newVal
            },
            onAutorization: function (newVal, oldVal) {
                this.changeOnAutorizationDrawer(newVal)
            },
        },
        methods:{
            openPage(item){

                //this.changeOnAutorizationDrawer(this.onAutorization)
                console.log("openPage")
            },
            showProductList(){
                this.$router.push('/setting')
            },
        }
    }
</script>
