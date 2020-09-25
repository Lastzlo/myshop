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



        <v-container id="dialog">
            <v-dialog v-model="dialog" max-width="500px">
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

                                    <v-file-input
                                            v-model="files"
                                            multiple
                                            label="Загрузите фото"
                                    ></v-file-input>



                                    <directory-list @selected-tags="selectedTags" :tegsFromProduct="tegsFromProduct"/>

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
                            <v-btn
                                    color="primary"
                                    dark
                                    class="mb-2"
                                    @click="addItem"
                            >New Product</v-btn>
                            <!--<v-dialog v-model="dialog" max-width="500px">
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

                                                    <directory-list @selected-tags="selectedTags" :tegsFromProduct="tegsFromProduct"/>

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
                            </v-dialog>-->
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
            DirectoryList
        },
        data: () => ({
            headers: [
                {
                    text: 'Product name',
                    align: 'start',
                    sortable: false,
                    value: 'productName',
                },{
                    text: 'Creation date',
                    sortable: false,
                    value: 'creationDate',
                },
                { text: 'Actions', value: 'actions', sortable: false },
            ],
            products: [],
            tegsFromProduct:[],
            dialog: false,
            id: -1,
            editedItem: {
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
            //переменная для файла
            files: []
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
                let product = this.editedItem

                if (this.id > -1) {
                    //update
                    this.$resource('/product{/id}').update({id: this.id}, product).then(result =>
                        result.json().then(data => {
                            const index = this.products.findIndex(item => item.id === data.id)
                            this.products.splice(index, 1, data)
                        })
                    )
                } else {
                    let formData = new FormData();

                    this.files.forEach(
                        file => {
                            formData.append('files', file)
                        }
                    )
                    formData.append(
                        'product',
                        new Blob(
                            [JSON.stringify(product)
                            ],
                            {
                                type: "application/json"
                            }
                        )
                    )

                    //save
                    this.$resource('/product{/id}').save({}, formData).then(
                        result =>
                            result.json().then(data => {
                                this.products.push(data)
                            })
                    )

                }
                this.closeDialog()

                this.files = [];
            },
            closeDialog () {
                this.dialog = false

                this.$nextTick(() => {
                    //очищает список отмеченых тегов
                    this.tegsFromProduct = []

                    this.editedItem = Object.assign({}, this.defaultItem)
                    this.id = -1
                })
            },

            addItem(){
                this.id = -1
                this.tegsFromProduct = []
                this.editedItem = Object.assign({}, this.defaultItem)
                this.dialog = true
            },

            initialize () {
                this.$resource('/product').get().then(result =>
                    result.json().then(data =>
                        //записать данные в products
                        data.forEach(product => this.products.push(product))
                    )
                )
            },

            deleteItem(item){
                this.$resource('/product{/id}').remove({id: item.id}).then(result => {
                    if (result.ok) {
                        this.products.splice(this.products.indexOf(item), 1)
                    }
                })
            },
            editItem(item){
                this.id = item.id
                this.tegsFromProduct = item.tags
                this.editedItem = Object.assign({}, item)
                this.dialog = true
            },
        },
        created: function () {
            this.initialize()
        },
    }
</script>

<style scoped>

</style>