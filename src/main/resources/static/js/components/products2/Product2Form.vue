<template>
    <v-card>
        <v-card-text>
            <v-form>
                <v-container id="categories-playground">
                    <v-combobox
                            v-model="category"
                            :filter="filter"
                            :hide-no-data="!searchCategory"
                            :items="categories"
                            item-value="categoryName"
                            item-text="categoryName"
                            :search-input.sync="searchCategory"

                            color="blue-grey lighten-2"
                            label="Select category or create new"

                            clearable
                    >
                        <template v-slot:no-data>
                            <v-list-item>
                                <span class="subheading">Create</span>
                                <v-chip
                                        label
                                        small
                                >
                                    {{ searchCategory }}
                                </v-chip>
                            </v-list-item>
                        </template>
                        <template v-slot:selection="{item}">
                            <v-chip
                                    v-if="item === Object(item)"
                                    label
                                    small
                            >
                                <span class="pr-2">
                                    {{ item.categoryName }}
                                </span>
                                <v-icon
                                        small
                                        @click="clearCategory"
                                >mdi-close</v-icon>
                            </v-chip>
                        </template>
                        <template v-slot:item="{ index, item }">
                            <v-text-field
                                    v-if="editing === item"
                                    v-model="editing.categoryName"
                                    autofocus
                                    flat
                                    background-color="transparent"
                                    hide-details
                                    solo
                                    @keyup.enter="edit(index, item)"
                            ></v-text-field>
                            <v-chip
                                    v-else
                                    dark
                                    label
                            >
                                {{ item.categoryName }}
                            </v-chip>
                            <v-spacer></v-spacer>
                            <v-list-item-action @click.stop>
                                <v-btn
                                        icon
                                        small
                                        @click.stop.prevent="edit(index, item)"
                                >
                                    <v-icon>{{ editing !== item ? 'mdi-pencil' : 'mdi-check' }}</v-icon>
                                </v-btn>
                                <v-btn
                                        icon
                                        small
                                        @click.stop.prevent="delCategory(item)"
                                >
                                    <v-icon>mdi-delete</v-icon>
                                </v-btn>


                            </v-list-item-action>
                        </template>
                    </v-combobox>
                </v-container>

                <v-container id="brands-playground">
                    <v-combobox
                            v-model="brand"
                            :filter="filter"
                            :hide-no-data="!searchBrand"
                            :items="brands"
                            item-value="brandName"
                            item-text="brandName"
                            :search-input.sync="searchBrand"
                            hide-selected
                            color="blue-grey lighten-2"
                            label="Select brand or create new"
                            clearable

                    >
                        <template v-slot:no-data>
                            <v-list-item>
                                <span class="subheading">Create</span>
                                <v-chip
                                        label
                                        small
                                >
                                    {{ searchBrand }}
                                </v-chip>
                            </v-list-item>
                        </template>
                        <template v-slot:selection="{item}">
                            <v-chip
                                    v-if="item === Object(item)"
                                    label
                                    small
                            >
                                <span class="pr-2">
                                    {{ item.brandName }}
                                </span>
                                <v-icon
                                        small
                                        @click="clearBrand"
                                >mdi-close</v-icon>
                            </v-chip>
                        </template>
                        <template v-slot:item="{ index, item }">
                            <v-text-field
                                    v-if="editing === item"
                                    v-model="editing.brandName"
                                    autofocus
                                    flat
                                    background-color="transparent"
                                    hide-details
                                    solo
                                    @keyup.enter="edit(index, item)"
                            ></v-text-field>
                            <v-chip
                                    v-else
                                    dark
                                    label
                            >
                                {{ item.brandName }}
                            </v-chip>
                            <v-spacer></v-spacer>
                            <v-list-item-action @click.stop>
                                <v-btn
                                        icon
                                        @click.stop.prevent="edit(index, item)"
                                >
                                    <v-icon>{{ editing !== item ? 'mdi-pencil' : 'mdi-check' }}</v-icon>
                                </v-btn>
                                <v-btn
                                        icon
                                        small
                                        @click.stop.prevent="delBrand(item)"
                                >
                                    <v-icon>mdi-delete</v-icon>
                                </v-btn>
                            </v-list-item-action>
                        </template>
                    </v-combobox>
                </v-container>

                <v-container id="product-playground">
                    <v-text-field
                        v-model="productName"
                        label="Product name"
                    ></v-text-field>
                </v-container>
            </v-form>
        </v-card-text>

        <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="blue darken-1" text v-on:click="save">Save</v-btn>
        </v-card-actions>
    </v-card>
</template>

<script>
    export default {
        name: "Product2Form",
        props: ['categories', 'brands','products'],
        data: function() {
            return {
                id: '',
                productName: '',   //переменная для текста
                category: null,
                brand: null,
                editing: null,
                searchCategory: null,
                searchBrand: null,
                index: -1,
            }
        },

        watch: {
            category (val, prev) {
                if(val === undefined) return
                if(typeof val === 'string') {
                    this.category = {
                        categoryName: val
                    }
                }
                /*console.log("Test watch")
                console.log(val)
                console.log(prev)*/
            },
            brand (val, prev) {
                if(val === undefined) return
                if(typeof val === 'string') {
                    this.brand = {
                        brandName: val
                    }
                }
                // if (val.length === prev.length) return
                //
                // this.brand = val.map(v => {
                //     if (typeof v === 'string') {
                //         v = {
                //             brandName: v,
                //         }
                //
                //         //нужно еще добавить сохранение на сервер
                //         this.brands.push(v)
                //     }
                //
                //     return v
                // })
            },
        },

        methods: {
            clearCategory(){
                this.category = undefined
            },
            clearBrand(){
                this.brand = undefined
            },
            delCategory(item){

                if(item.id){
                    this.$resource('/category{/id}').remove({id: item.id}).then(result => {
                        if (result.ok) {
                            this.categories.splice(this.categories.indexOf(item), 1)

                        }
                    })
                }else {
                    this.categories.splice(this.categories.indexOf(item), 1)
                }

            },
            delBrand(item){

                if(item.id){
                    this.$resource('/brand{/id}').remove({id: item.id}).then(result => {
                        if (result.ok) {
                            this.categories.splice(this.categories.indexOf(item), 1)

                        }
                    })
                }else {
                    this.categories.splice(this.categories.indexOf(item), 1)
                }

            },


            save() {

                let product = {
                    productName: this.productName,
                    brand: this.brand,
                    category: this.category

                };


                this.$resource('/product{/id}').save({}, product).then(
                    result =>
                        result.json().then(data => {
                            this.products.push(data);
                            this.productName = '';


                        })
                )

            },
            edit (index, item) {
                if (!this.editing) {
                    this.editing = item
                    this.index = index
                } else {
                    this.editing = null
                    this.index = -1
                }
            },
            filter (item, queryText, itemText) {
                if (item.header) return false

                const hasValue = val => val != null ? val : ''

                const text = hasValue(itemText)
                const query = hasValue(queryText)

                return text.toString()
                    .toLowerCase()
                    .indexOf(query.toString().toLowerCase()) > -1
            },
        }
    }
</script>

<style scoped>

</style>