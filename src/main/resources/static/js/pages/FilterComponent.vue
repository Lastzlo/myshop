<template>
    <v-container fluid>
        <v-container>
            <v-row>
                <v-col>
                    <v-card
                            class="d-flex"
                            color="grey lighten-2"
                            flat
                            tile
                    >
                        <v-card
                                outlined
                                tile
                        >
                            <v-card
                                    width="125px"
                                    height="125px"
                            ></v-card>
                        </v-card>
                        <v-card
                                outlined
                                tile
                        >
                            <v-card-title>
                                Name of product
                            </v-card-title>
                            <v-card-subtitle>Описание товара</v-card-subtitle>
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
                                <v-card-title>
                                    Цена
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

            productsApi.getAll().then(result =>
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