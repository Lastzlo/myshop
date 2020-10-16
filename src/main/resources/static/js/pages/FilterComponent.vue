<template>
    <v-container fluid>
        //добавить панель для фильтра, она будет видна если экран больше 960

        <v-container
                fluid
                id="productList"

        >
            <v-row >
                <v-col
                        class="pa-0"
                        cols="3"
                >
                    <filter-form @selected-tags="selectedTags" :tegsFromProduct="tegsFromProduct"/>

                </v-col>
                <v-col
                        class="pa-0"
                        cols="9"
                >
                    <v-col
                            class="pa-1"
                            v-for="product in products"
                            :key="product.id"

                            v-resize="onResize"

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
                                        @click="openPage2(product)"

                                ></v-img>

                            </v-card>

                            <v-card
                                    outlined
                                    tile
                                    max-width="800"
                            >
                                <v-card-title
                                        v-text="product.productName"
                                        @click="openPage2(product)"
                                >
                                    Name of product
                                </v-card-title>
                                <v-card-subtitle
                                        v-text="product.productDiscription"
                                >
                                    iscription of product
                                </v-card-subtitle>
                                <v-card-text>
                                    <v-chip-group
                                            column
                                    >
                                        <v-chip
                                                v-for="tag in product.tags"
                                                :key="tag.id"
                                                @click="openPage(tag)"
                                                small
                                        >
                                            {{tag.name}}
                                        </v-chip>

                                    </v-chip-group>
                                </v-card-text>
                            </v-card>

                            <v-card
                                    class="ml-auto"
                                    outlined
                                    tile
                            >
                                <div class="d-flex flex-column align-center">
                                    <v-card-title
                                            v-text="product.price+' грн'"
                                            @click="openPage2(product)"
                                    >
                                        Price
                                    </v-card-title>
                                    <v-btn
                                            text
                                            @click="openPage2(product)"
                                    >
                                        Купить
                                    </v-btn>
                                </div>
                            </v-card>
                        </v-card>
                    </v-col>
                </v-col>

            </v-row>
        </v-container>

    </v-container>

</template>

<script>
    import directoriesApi from "api/directories.js";
    import FilterForm from "components/other/FilterForm.vue"

    export default {
        name: "FilterComponent",
        components: {
            FilterForm
        },
        data: () => ({
            products: [],
            directoryId: null,
            vCradClass: "",

            tegsFromProduct:[],
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
            selectedTags(tags){
                //
            },
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
            openPage2(item){
                console.log("openPage2")
                //переход на страницу и обработана ошибка
                //this.$router.push({ path: `/filter/${item.id}` }, () => {})
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