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

        <!--<v-container>
            &lt;!&ndash;диалог для добавления товара&ndash;&gt;
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
        </v-container>-->

        <v-container id="product-table">
            <!--тут будет список товаров-->
            <template>
                <v-data-table
                        :headers="headers"
                        :items="products"
                        sort-by="calories"
                        class="elevation-1"
                >
                    <template v-slot:top>
                        <v-toolbar flat color="white">
                            <v-toolbar-title>Product list</v-toolbar-title>
                            <v-divider
                                    class="mx-4"
                                    inset
                                    vertical
                            ></v-divider>
                            <v-spacer></v-spacer>
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
                        </v-toolbar>
                    </template>
                    <template v-slot:item.actions="{ item }">
                        <v-icon
                                small
                                class="mr-2"
                                @click="editItem(item)"
                        >
                            mdi-pencil
                        </v-icon>
                        <v-icon
                                small
                                @click="deleteItem(item)"
                        >
                            mdi-delete
                        </v-icon>
                    </template>
                    <template v-slot:no-data>
                        <v-btn color="primary" @click="initialize">Reset</v-btn>
                    </template>
                </v-data-table>
            </template>
        </v-container>

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
            headers: [
                // {
                //     text: 'Dessert (100g serving)',
                //     align: 'start',
                //     sortable: false,
                //     value: 'name',
                // },
                // { text: 'Calories', value: 'calories' },
                // { text: 'Fat (g)', value: 'fat' },
                // { text: 'Carbs (g)', value: 'carbs' },
                // { text: 'Protein (g)', value: 'protein' },
                // { text: 'Actions', value: 'actions', sortable: false },
            ],
            products: [],

            items:[],
            clearSelected: false,
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
            },
            initialize () {
                /*this.products = [
                    {
                        name: 'Frozen Yogurt',
                        calories: 159,
                        fat: 6.0,
                        carbs: 24,
                        protein: 4.0,
                    },
                    {
                        name: 'Ice cream sandwich',
                        calories: 237,
                        fat: 9.0,
                        carbs: 37,
                        protein: 4.3,
                    },
                    {
                        name: 'Eclair',
                        calories: 262,
                        fat: 16.0,
                        carbs: 23,
                        protein: 6.0,
                    },
                    {
                        name: 'Cupcake',
                        calories: 305,
                        fat: 3.7,
                        carbs: 67,
                        protein: 4.3,
                    },
                    {
                        name: 'Gingerbread',
                        calories: 356,
                        fat: 16.0,
                        carbs: 49,
                        protein: 3.9,
                    },
                    {
                        name: 'Jelly bean',
                        calories: 375,
                        fat: 0.0,
                        carbs: 94,
                        protein: 0.0,
                    },
                    {
                        name: 'Lollipop',
                        calories: 392,
                        fat: 0.2,
                        carbs: 98,
                        protein: 0,
                    },
                    {
                        name: 'Honeycomb',
                        calories: 408,
                        fat: 3.2,
                        carbs: 87,
                        protein: 6.5,
                    },
                    {
                        name: 'Donut',
                        calories: 452,
                        fat: 25.0,
                        carbs: 51,
                        protein: 4.9,
                    },
                    {
                        name: 'KitKat',
                        calories: 518,
                        fat: 26.0,
                        carbs: 65,
                        protein: 7,
                    },
                ]*/

                this.$resource('/product').get().then(result =>
                    result.json().then(data =>
                        //записать данные в products
                        data.forEach(product => this.products.push(product))
                    )
                )


            },
        },

        created: function () {
            //запрос на сервер

            this.initialize()

        },
    }
</script>

<style scoped>

</style>