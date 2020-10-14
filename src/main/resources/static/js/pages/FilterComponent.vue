<template>
    <v-container fluid>
        <v-container>
            <v-row>
                <v-col
                        v-for="product in products"
                        :key="product.id"
                        cols="12"
                >
                    <v-card
                            class="d-flex"
                            color="grey lighten-2"
                            flat
                            tile
                    >
                        <!--<v-card
                                outlined
                                tile
                        >
                            <v-card
                                    width="125px"
                                    height="125px"
                            ></v-card>
                        </v-card>-->
                        <v-avatar
                                size="125"
                                tile
                        >
                            <v-img v-if="product.photos.length>0" :src="product.photos[0]"></v-img>
                        </v-avatar>
                        <v-card
                                outlined
                                tile
                        >
                            <v-card-title v-text="product.productName">
                                Name of product
                            </v-card-title>
                            <v-card-subtitle v-text="product.productDiscription">Discription of product</v-card-subtitle>
                        </v-card>
                        <!--<v-card
                                class="ml-auto align-self-center"
                                outlined
                                tile
                        >
                            <div class="d-flex flex-column">
                                <v-card-title>
                                    Цена
                                </v-card-title>
                                <v-btn text>
                                    Купить
                                </v-btn>
                            </div>
                        </v-card>-->
                        <v-card
                                class="ml-auto"
                                outlined
                                tile
                        >
                            <div class="d-flex flex-column">
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
    import productsApi from "../api/products";

    export default {
        name: "FilterComponent",
        data: () => ({
            products: [],
        }),
        created() {
            let categoryId = this.$route.params.id

            console.log("categoryId = "+categoryId)

            // productsApi.getAll().then(result =>
            //     result.json().then(data =>
            //         //записать данные в products
            //         data.forEach(product => this.products.push(product))
            //     )
            // )

            productsApi.getProductsByTagId(categoryId).then(result =>
                result.json().then(data =>
                    //записать данные в products
                    data.forEach(product => this.products.push(product))
                )
            )
        }

    }
</script>

<style scoped>

</style>