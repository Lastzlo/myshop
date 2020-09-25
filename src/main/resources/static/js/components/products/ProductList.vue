<template>
    <v-container fluid>
        <v-container id="product-form">
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

                                <v-container
                                        id="photos"
                                        v-if="this.photoToShow.length>0"
                                >
                                    <v-row>
                                        <v-col
                                                v-for="card in this.photoToShow"
                                                :key="card.src"

                                        >
                                            <v-card>
                                                <v-img
                                                        :src="card.src"
                                                        height="200px"
                                                        width="200px"
                                                >
                                                    <!--<v-card-title v-text="card.title"></v-card-title>-->
                                                </v-img>

                                                <!--<v-card-actions>
                                                    <v-spacer></v-spacer>

                                                    <v-btn icon>
                                                        <v-icon>mdi-heart</v-icon>
                                                    </v-btn>

                                                    <v-btn icon>
                                                        <v-icon>mdi-bookmark</v-icon>
                                                    </v-btn>

                                                    <v-btn icon>
                                                        <v-icon>mdi-share-variant</v-icon>
                                                    </v-btn>
                                                </v-card-actions>-->
                                            </v-card>
                                        </v-col>
                                    </v-row>
                                </v-container>


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
                    <v-btn color="blue darken-1" text @click="clearForm">Clear</v-btn>
                    <v-btn color="blue darken-1" text @click="save">{{ saveButtonTitle }}</v-btn>
                </v-card-actions>
            </v-card>
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
            id: -1,
            editedItem: {
                productName: '',
                tags: [],
                photos: [],
                //price: '',
                //productDiscription: '',
            },
            defaultItem: {
                //category: null,
                //brand: null,
                productName: '',
                tags: [],
                photos:[],
                //price: '',
                //productDiscription: '',
            },
            //переменная для файла
            files: [],
            photoToShow:[],
            photoToDelete:[],
        }),
        computed: {
            formTitle () {
                return this.id === -1 ? 'New Item' : 'Edit Item'
            },
            saveButtonTitle () {
                return this.id === -1 ? 'Save' : 'Update'
            },
        },
        watch: {
            // files(newVal, oldVal){
            //
            //     newVal.forEach(
            //
            //         item => this.photoToShow.push(
            //             window.URL.createObjectURL(item)
            //         )
            //     )
            // }

        },
        methods: {
            getImgUrl(pic) {
                let url = '/img/'+pic.toString()
                return url
            },
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
                this.clearForm()

                this.files = [];
            },
            clearForm () {


                this.$nextTick(() => {
                    //очищает список отмеченых тегов
                    this.tegsFromProduct = []

                    this.editedItem = Object.assign({}, this.defaultItem)
                    this.id = -1

                    this.photoToShow = []
                })
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

                if(item.photos !== null){
                    this.photoToShow = item.photos
                }


            },
        },
        created: function () {
            this.initialize()
        },
    }
</script>

<style scoped>

</style>