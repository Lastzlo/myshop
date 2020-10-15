<template>
    <v-navigation-drawer
            id="catalogDrawer"
            v-model="onCatalog"
            style="position:fixed; overflow-y:scroll;"
            absolute
            temporary
    >
        <!--<v-navigation-drawer
                dark
                v-model="drawer"
                width="400"
                style="position:fixed; top:0; left:0; overflow-y:scroll;"
        >-->
        <v-list-item>
            <v-list-item-content>
                <v-list-item-title class="title">
                    Каталог товаров
                </v-list-item-title>
            </v-list-item-content>
        </v-list-item>
        <v-divider></v-divider>

        <v-list
                nav
                dense
        >
            <v-list-item

                    v-for="item in items"
                    :key="item.title"
                    link
                    @click="openPage(item)"
            >
                <v-list-item-content>
                    <v-list-item-title>{{ item.name }}</v-list-item-title>
                </v-list-item-content>
            </v-list-item>
        </v-list>
    </v-navigation-drawer>
</template>
<script>
    import directoriesApi from 'api/directories'
    import {mapState, mapMutations} from 'vuex'

    export default {
        name: 'CatalogDrawerComponent',
        data: () => ({
            items: [],
            onCatalog: false
        }),
        computed: mapState(['onCatalogDrawer']),
        watch: {
            onCatalogDrawer: function (newVal, oldVal) {
                this.onCatalog = newVal
            },
            onCatalog: function (newVal, oldVal) {
                if(newVal!==this.onCatalogDrawer){
                    this.changeCatalogDrawerMutation()
                }
            },
        },
        methods:{
            ...mapMutations(['changeCatalogDrawerMutation']),
            openPage(item){
                console.log("openCatalogPage")

                //переход на страницу и обработана ошибка
                this.$router.push({ path: `/filter/${item.id}` }, () => {})

            },
        },
        created: function () {
            //запрос на сервер
            directoriesApi.getCore().then(result =>
                result.json().then(data => {
                    if(data.children){
                        data.children.forEach(
                            item => {
                                this.items.push(item)
                            }
                        )
                    }
                })
            )
            // this.$resource('/directory/getCore').get().then(result =>
            //     result.json().then(data => {
            //         if(data.children){
            //             data.children.forEach(
            //                 item => {
            //                     this.items.push(item)
            //                 }
            //             )
            //         }
            //     })
            // )
        },

    }
</script>