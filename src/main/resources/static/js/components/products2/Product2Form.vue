<template>
    <v-card>
        <v-card-text>
            <v-form>
                <v-container id="categories-playground">
                    <v-autocomplete
                            :items="categories"
                            color="blue-grey lighten-2"
                            label="Select category"
                            item-text="categoryName"
                            item-value="categoryName"
                    >
                        <template v-slot:item="data">
                            <v-list-item-content>
                                <v-list-item-title v-html="data.item.categoryName"></v-list-item-title>

                            </v-list-item-content>
                        </template>
                    </v-autocomplete>
                </v-container>

                <v-container id="brands-playground">
                    <v-autocomplete
                            :items="brands"
                            color="blue-grey lighten-2"
                            label="Select brand"
                            item-text="brandName"
                            item-value="brandName"
                    >
                        <template v-slot:item="data">
                            <v-list-item-content>
                                <v-list-item-title v-html="data.item.brandName"></v-list-item-title>

                            </v-list-item-content>
                        </template>
                    </v-autocomplete>
                </v-container>

                <v-text-field  label="Product name"></v-text-field>
            </v-form>
        </v-card-text>

        <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="blue darken-1" text >Save</v-btn>
        </v-card-actions>
    </v-card>
</template>

<script>
    export default {
        name: "Product2Form",
        props: ['categories', 'brands'],
        data(){

        },
        created() {
        },
    }
</script>

<style scoped>

</style>