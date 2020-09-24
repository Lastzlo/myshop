<template>
    <v-container fluid>
        <!--<v-container id="categories-playground">
            <category-autocomplete :categories="categories" @select-category="selectCategory"/>
        </v-container>

        <v-container id="brands-playground" v-if="selectedCategory.brands.length>0" >
            <brand-autocomplete :brands="selectedCategory.brands" @select-brand="selectBrand"/>
        </v-container>-->

        <!--<v-container id="product-playground">
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
        </v-container>-->

        <!--<v-container id="product-table">
            &lt;!&ndash;тут будет список товаров&ndash;&gt;
        </v-container>-->

        <v-container>
            <v-dialog v-model="dialog" max-width="500px">
                <template v-slot:activator="{ on, attrs }">
                    <v-btn
                            color="primary"
                            dark
                            class="mb-2"
                            v-bind="attrs"
                            v-on="on"
                    >New Product</v-btn>
                </template>
                <v-card>
                    <v-card-title>
                        <span class="headline">{{ formTitle }}</span>
                    </v-card-title>

                    <v-card-text>
                        <v-container>
                            <v-row>
                                <v-col>
                                    <v-text-field
                                            v-model="editedItem.productName"
                                            label="Product name"
                                            autofocus
                                    ></v-text-field>

                                    <directory-list @selected-tags="selectedTags" :clearSelected="clearSelected"/>

                                </v-col>
                            </v-row>
                        </v-container>
                    </v-card-text>

                    <v-card-actions>
                        <v-spacer></v-spacer>
                        <v-btn color="blue darken-1" text @click="closeDialog">Cancel</v-btn>
                        <v-btn color="blue darken-1" text @click="save">Save</v-btn>
                    </v-card-actions>
                </v-card>
            </v-dialog>
        </v-container>

        <!--<v-container id="product-table">
            &lt;!&ndash;тут будет список товаров&ndash;&gt;
        </v-container>-->

    </v-container>



</template>

<script>
    //import CategoryAutocomplete from "../categories/CategoryAutocomplete.vue";
    //
    // import BrandAutocomplete from "../brands/BrandAutocomplete.vue";

    import DirectoryList from "components/other/DirectoryList.vue";

    export default {
        name: "ProductList",
        components: {
            // CategoryAutocomplete,
            // BrandAutocomplete,
            //DirectoryList2,
            DirectoryList
        },
        data: () => ({
            items:[],
            clearSelected: false,
            // selectedCategory: {
            //     brands:{}
            // },
            // selectedBrand: null,
            //selectedTags: [],
            dialog: false,
            id: -1,
            editedItem: {
                //category: null,
                //brand: null,
                productName: '',
                tags: []
                //price: '',
                //productDiscription: '',
            },
            defaultItem: {
                //category: null,
                //brand: null,
                productName: '',
                tags: []
                //price: '',
                //productDiscription: '',
            },
        }),
        computed: {
            formTitle () {
                return this.id === -1 ? 'New Item' : 'Edit Item'
            },
        },
        methods: {
            selectedTags(tags){
                this.editedItem.tags = tags;
            },

            // selectCategory(category) {
            //     this.selectedCategory = category;
            // },
            // selectBrand(brand) {
            //     this.selectedBrand = brand;
            // },
            save () {
                //this.editedItem.category = this.selectedCategory
                //this.editedItem.brand = this.selectedBrand
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
                this.closeDialog()
            },
            closeDialog () {

                //console.log("this.editedItem.tags = "+this.editedItem.tags)

                this.dialog = false

                this.clearSelectedTags()

                this.$nextTick(() => {
                    this.editedItem = Object.assign({}, this.defaultItem)
                    this.id = -1
                })
            },
            //очищает список отмеченых тегов
            clearSelectedTags (){
                this.clearSelected = !this.clearSelected
            }
        },

        created: function () {
            //запрос на сервер

        },
    }
</script>

<style scoped>

</style>