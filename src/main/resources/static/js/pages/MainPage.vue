<template>
    <v-app>
        <v-app-bar app dense>

            <v-app-bar-nav-icon @click.stop="catalogDrawer = !catalogDrawer"></v-app-bar-nav-icon>

            <v-btn icon>
                <v-icon>mdi-magnify</v-icon>
            </v-btn>

            <v-spacer></v-spacer>

            <v-btn text>
                My Shop
            </v-btn>

            <v-spacer></v-spacer>

            <v-btn icon @click.stop="autorizationDrawer = !autorizationDrawer">
                <v-icon>mdi-dots-vertical</v-icon>
            </v-btn>

        </v-app-bar>

        <v-navigation-drawer
                id="catalogDrawer"
                v-model="catalogDrawer"
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
                        @click="openPage(item)"
                >
                    <v-list-item-content>
                        <v-list-item-title>{{ item.name }}</v-list-item-title>
                    </v-list-item-content>
                </v-list-item>
            </v-list>
        </v-navigation-drawer>

        <v-navigation-drawer
                id="autorizationDrawer"
                v-model="autorizationDrawer"
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
                <v-list-item @click="openPage(null)">
                    <v-list-item-title>Войдти через Google</v-list-item-title>
                </v-list-item>

                <v-list-item @click="openPage(null)">
                    <v-list-item-title>Войдти через Facebook</v-list-item-title>
                </v-list-item>

                <v-list-item @click="openPage(null)">
                    <v-list-item-title>Регистрация</v-list-item-title>
                </v-list-item>
            </v-list>
        </v-navigation-drawer>

        <v-main>
            <v-card
                    class="mx-auto"
                    max-width="768"
                    v-resize="onResize"
            >
                <v-container>
<!--                    <v-subheader>Window Size</v-subheader>-->
<!--                    {{ windowSize }}-->

                    <v-row
                            dense
                    >
                        <v-col
                                v-for="item in items"
                                :key="item.id"
                                :cols=cols


                        >
                            <v-card
                                    class="d-flex align-end justify-center pa-0 mx-auto"
                                    height="125px"
                                    @click="openPage(item)"
                                    outlined
                            >
                                <div :class="`text-body-2 mx-2`">
                                    {{item.name}}
                                </div>

                            </v-card>
                        </v-col>
                    </v-row>
                </v-container>
            </v-card>
        </v-main>
    </v-app>

</template>

<script>
    export default {
        name: "MainPage",
        data: () => ({
            catalogDrawer: false,
            autorizationDrawer: false,
            items: [],
            // windowSize: {
            //     x: 0,
            //     y: 0,
            // },
            cols: 4,
        }),
        mounted () {
            this.onResize()
        },
        methods:{
            openPage(item){
                console.log("openPage")
                this.catalogDrawer = false
                this.autorizationDrawer = false
            },
            onResize () {
                //this.windowSize = { x: window.innerWidth, y: window.innerHeight }
                let x = window.innerWidth;

                //перебор
                if(x<240){
                    this.cols = 12;
                }
                //перебор
                if(x>=240 && x<360){
                    this.cols = 6;
                }

                if(x>=360 && x<480){
                    this.cols = 4;
                }else if(x>=480 && x<720){
                    this.cols = 3;
                } else if(x>=720){
                    this.cols = 2;
                }

            },
        },
        watch: {
            // group () {
            //     this.catalogDrawer = false
            //     this.autorizationDrawer = false
            // },
        },
        created: function () {
            //запрос на сервер
            this.$resource('/directory/getCore').get().then(result =>
                result.json().then(data => {
                    //this.items = data.children
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

<style scoped>

</style>