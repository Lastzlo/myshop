<template>
    <v-card>
        <v-card-text>
            <v-form>
                <v-container id="categories-playground">
                    <v-combobox
                            v-model="model"
                            :filter="filter"
                            :hide-no-data="!search"
                            :items="categories"
                            :search-input.sync="search"
                            hide-selected
                            color="blue-grey lighten-2"
                            label="Select category"
                            multiple
                            small-chips
                            solo
                    >

                        <!--<template v-slot:no-data>
                            <v-list-item>
                                <span class="subheading">Create</span>
                                <v-chip
                                        label
                                        small
                                >
                                    {{ search }}
                                </v-chip>
                            </v-list-item>
                        </template>-->

                        <template v-slot:selection="{ attrs, item, parent, selected }">
                            <v-chip
                                    v-if="item === Object(item)"
                                    v-bind="attrs"
                                    :input-value="selected"
                                    label
                                    small
                            >
                                <span class="pr-2">
                                    {{ item.categoryName }}
                                </span>
                                <v-icon
                                        small
                                        @click="parent.selectItem(item)"
                                >close</v-icon>
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
                                    small
                            >
                                {{ item.categoryName }}
                            </v-chip>
                            <v-spacer></v-spacer>
                            <v-list-item-action @click.stop>
                                <v-btn
                                        icon
                                        @click.stop.prevent="edit(index, item)"
                                >
                                    <v-icon>{{ editing !== item ? 'mdi-pencil' : 'mdi-check' }}</v-icon>
                                </v-btn>
                            </v-list-item-action>
                        </template>

                        <!--<template v-slot:item="data">
                            <v-list-item-content>
                                <v-list-item-title v-html="data.item.categoryName"></v-list-item-title>

                            </v-list-item-content>
                        </template>-->
                    </v-combobox>
                </v-container>

                <v-container id="brands-playground">
                    <v-autocomplete
                            :items="brands"
                            color="blue-grey lighten-2"
                            label="Select brand"
                            item-text="brandName"
                            item-value="brandName"
                            v-model="brandName"
                    >
                        <template v-slot:item="data">

                            <v-list-item-content>
                                <v-list-item-title v-html="data.item.brandName"></v-list-item-title>

                            </v-list-item-content>
                        </template>
                    </v-autocomplete>
                </v-container>

                <v-container>
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
                categoryName: '',
                model: [],
                brandName: '',
                editing: null,
                search: null,
                index: -1,

            }
        },

        watch: {
            model (val, prev) {
                if (val.length === prev.length) return

                this.model = val.map(v => {
                    if (typeof v === 'string') {
                        v = {
                            categoryName: v,
                        }

                        console.log(v)
                        this.categories.push(v)
                    }

                    return v
                })
            },
        },

        methods: {
            save() {

                let product = {
                    productName: this.productName,
                    brandName: this.brandName,
                    categoryName: this.model

                };
                // let formData = new FormData();
                //
                //
                // formData.append(
                //     'product',
                //     new Blob(
                //         [JSON.stringify(product)
                //         ],
                //         {
                //             type: "application/json"
                //         }
                //     )
                // )

                //saveProduction имеет кастомную ссылку 'product/create'
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