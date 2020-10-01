<template>
    <v-app>

        <v-app-bar app dense>

            <v-app-bar-nav-icon @click.stop="drawer = !drawer"></v-app-bar-nav-icon>

            <v-btn icon>
                <v-icon>mdi-magnify</v-icon>
            </v-btn>

            <v-spacer></v-spacer>

            <v-btn text>
                My Shop
            </v-btn>

            <v-spacer></v-spacer>

            <v-btn icon>
                <v-icon>mdi-dots-vertical</v-icon>
            </v-btn>

        </v-app-bar>

        <v-navigation-drawer
                v-model="drawer"
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
                >
                    <v-list-item-content>
                        <v-list-item-title>{{ item.name }}</v-list-item-title>
                    </v-list-item-content>

                    <v-list-item-group v-model="group"></v-list-item-group>
                </v-list-item>
            </v-list>
        </v-navigation-drawer>

        <v-main>

        </v-main>

    </v-app>

</template>

<script>
    export default {
        name: "MainPage",
        data: () => ({
            drawer: false,
            group: null,
            items: [
            ],
        }),
        watch: {
            group () {
                this.drawer = false
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