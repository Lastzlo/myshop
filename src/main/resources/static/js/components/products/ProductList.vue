<template>
    <v-container fluid>
                <v-container id="categories-playground">
                    <category-autocomplete :categories="categories" @select-category="selectCategory"/>
                </v-container>

                <v-container id="brands-playground" v-if="selectedCategory.brands.length>0" >
                    <brand-autocomplete :brands="selectedCategory.brands" @select-brand="selectBrand"/>
                </v-container>

                <v-container id="product-playground">
                    <v-text-field
                            v-model="editedItem.productName"
                            label="Product name"
                    ></v-text-field>
                    <v-text-field
                            v-model="editedItem.price"
                            label="price(грн)"
                    ></v-text-field>
                    <v-text-field
                            v-model="editedItem.productDiscription"
                            label="product Discription"
                    ></v-text-field>

                    <v-btn color="blue darken-1" text @click="save">Save</v-btn>
                </v-container>




    </v-container>
</template>

<script>
    import CategoryAutocomplete from "../categories/CategoryAutocomplete.vue";
    import BrandAutocomplete from "../brands/BrandAutocomplete.vue";

    export default {
        name: "ProductList",
        components: {
            CategoryAutocomplete,
            BrandAutocomplete,
        },
        props: ['categories'],
        data: () => ({
            selectedCategory: {
                brands:{}
            },
            selectedBrand: null,
            dialog: false,
            id: -1,
            editedItem: {
                category: null,
                brand: null,
                productName: '',
                price: '',
                productDiscription: '',
            },
            defaultItem: {
                category: null,
                brand: null,
                productName: '',
                price: '',
                productDiscription: '',
            },
        }),
        methods: {
            selectCategory(category) {
                this.selectedCategory = category;
            },
            selectBrand(brand) {
                this.selectedBrand = brand;
            },
            save () {
                this.editedItem.category = this.selectedCategory
                this.editedItem.brand = this.selectedBrand
                let product = this.editedItem

                if (this.id > -1) {
                    this.$resource('/product{/id}').update({id: this.id}, product).then(result =>
                        result.json().then(data => {
                            //var index = getIndex(this.selectedCategory.brands, data.id)
                            //this.selectedCategory.brands.splice(index, 1, data)
                        })
                    )
                } else {
                    this.$resource('/product{/id}').save({}, product).then(
                        result =>
                            result.json().then(data => {
                                //this.selectedCategory.brands.push(data)
                            })
                    )
                }
                this.close()
            },
            close () {
                this.dialog = false
                this.$nextTick(() => {
                    this.editedItem = Object.assign({}, this.defaultItem)
                    this.id = -1
                })
            },
        }
    }
</script>

<style scoped>

</style>