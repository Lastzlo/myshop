<template>
    <v-container fluid>
        //добавить панель для фильтра, она будет видна если экран больше 960


        <v-container>
            <v-row>
                <v-col
                        v-for="product in products"
                        :key="product.id"

                        v-resize="onResize"

                        cols="12"
                >
                    <v-card
                            :class=vCradClass
                            color="grey lighten-2"
                            flat
                            tile
                    >
                        <v-card

                                class="align-self-center"
                        >
                            <v-img
                                    v-if="product.photos.length>0"
                                    :src="product.photos[0]"
                                    contain
                                    height="150"
                                    width="150"


                            ></v-img>

                        </v-card>

                        <v-card
                                outlined
                                tile
                        >
                            <v-card-title v-text="product.productName">
                                Name of product
                            </v-card-title>
                            <v-card-text>
                                <v-chip-group
                                        column
                                >
                                    <v-chip
                                            v-for="tag in product.tags"
                                            :key="tag.id"
                                            @click="openPage(tag)"
                                    >
                                        {{tag.name}}
                                    </v-chip>

                                </v-chip-group>
                            </v-card-text>
                            <v-card-subtitle
                                    v-text="product.productDiscription"
                            >
                                iscription of product
                            </v-card-subtitle>
                        </v-card>

                        <v-card
                                class="ml-auto"
                                outlined
                                tile
                        >
                            <div class="d-flex flex-column align-center">
                                <v-card-title v-text="product.price">
                                    Price
                                </v-card-title>
                                <v-btn text>
                                    Купить
                                </v-btn>
                            </div>
                        </v-card>
                    </v-card>
                </v-col>
            </v-row>
        </v-container>

    </v-container>

</template>

<script>
    import directoriesApi from "api/directories.js";

    export default {
        name: "FilterComponent",
        data: () => ({
            products: [],
            directoryId: null,
            vCradClass: ""
        }),
        mounted () {
            this.onResize()
        },
        watch: {
            $route(to, from) {
                // обрабатываем изменение параметров маршрута...
                this.directoryId = to.params.id
                console.log("newDirectoryId = "+this.directoryId)

                this.setProducts(this.directoryId)
            }
        },
        methods :{
            setProducts(directoryId){

                this.products= []
                directoriesApi.getProductByDirectoryId(directoryId).then(result =>
                    result.json().then(data =>
                        //записать данные в products
                        data.forEach(product => this.products.push(product))
                    )
                )
            },
            openPage(item){
                console.log("openPage")
                //переход на страницу и обработана ошибка
                this.$router.push({ path: `/filter/${item.id}` }, () => {})
            },
            onResize () {
                let x = window.innerWidth;

                //console.log("window.innerWidth = "+window.innerWidth)

                if(x>660){
                    this.vCradClass = "d-flex"
                } else {
                    this.vCradClass = ""
                }

            },
        },
        created() {
            this.directoryId = this.$route.params.id
            console.log("directoryId = "+this.directoryId)

            this.setProducts(this.directoryId)

            // directoriesApi.getProductByDirectoryId(this.directoryId).then(result =>
            //     result.json().then(data =>
            //         //записать данные в products
            //         data.forEach(product => this.products.push(product))
            //     )
            // )
        }

    }
</script>

<style scoped>

</style>