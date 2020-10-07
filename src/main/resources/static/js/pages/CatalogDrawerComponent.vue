<template>
    <v-navigation-drawer
            id="catalogDrawer"
            v-model="onCatalog"
            absolute
            temporary
    >
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
                    @click="openPage"
            >
                <v-list-item-content>
                    <v-list-item-title>{{ item.name }}</v-list-item-title>
                </v-list-item-content>
            </v-list-item>
        </v-list>
    </v-navigation-drawer>
</template>
<script>
    export default {
        name: 'CatalogDrawerComponent',
        props: ['onCatalogDrawer'],
        data: () => ({
            items: [],
            onCatalog: false
        }),
        methods:{
            openPage(item){
                //window.location.href = 'http://localhost:9500/setting'
                console.log("openPage")
                this.onCatalog = false
            },
        },
        created: function () {
            //запрос на сервер
            this.$resource('/directory/getCore').get().then(result =>
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
        },

    }
</script>