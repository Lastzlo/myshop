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

                    <v-list-item-group v-model="group"></v-list-item-group>
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
                <v-list-item-group v-model="group">
                    <v-list-item @click="openPage(null)">
                        <v-list-item-title>Войдти через Google</v-list-item-title>
                    </v-list-item>

                    <v-list-item @click="openPage(null)">
                        <v-list-item-title>Войдти через Facebook</v-list-item-title>
                    </v-list-item>

                    <v-list-item @click="openPage(null)">
                        <v-list-item-title>Регистрация</v-list-item-title>
                    </v-list-item>
                </v-list-item-group>
            </v-list>
        </v-navigation-drawer>

        <v-main>
            <v-card
            ё        class="mx-auto"
                    max-width="768"
            >
                <v-container fluid>
                    <v-row
                            dense
                    >
                        <v-col
                                v-for="item in items"
                                :key="item.id"
                                :cols="4"
                        >
                            <v-card
                                    height="120px"
                                    width="120px"
                                    @click="openPage(item)"
                            >
                                <!--<v-img
                                        class="white&#45;&#45;text align-end"
                                        gradient="to bottom, rgba(0,0,0,.1), rgba(0,0,0,.5)"
                                        height="150px"
                                        width="150px"
                                >
                                    <v-card-title v-text="item.name"></v-card-title>
                                </v-img>-->

                                <v-card-subtitle v-text="item.name"></v-card-subtitle>
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
            group: null,
            items: [
            ],
        }),
        methods:{
            openPage(item){
                console.log("openPage")
            },
        },
        watch: {
            group () {
                this.catalogDrawer = false
                this.autorizationDrawer = false
            },
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