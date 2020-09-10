<template>
    <v-container>
        <v-form
                ref="form"
                lazy-validation
        >
            <v-row no-gutters>
                <v-col
                        cols="12"
                        sm="6"
                        md="8"
                >
                    <v-text-field
                            v-model="categoryName"
                            :rules="nameRules"
                            label="Add new category"
                            single-line
                            clearable
                    ></v-text-field>
                </v-col>
                <v-col
                        cols="6"
                        md="4"
                >
                    <v-btn
                            class="ma-2"
                            color="success"
                            @click="save"

                    >
                        Save
                    </v-btn>
                    <v-btn
                            class="ma-2"
                            color="error"
                            @click="clear"
                    >
                        Clear
                    </v-btn>
                </v-col>

            </v-row>
        </v-form>
        <v-form>
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
            >
                <template v-slot:no-data>
                    <v-list-item>
                        <v-list-item-content>
                            <v-list-item-title>
                                No results matching "<strong>{{ searchCategory }}</strong>". Press <kbd >enter</kbd> to create a new one
                            </v-list-item-title>
                        </v-list-item-content>
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
        </v-form>
    </v-container>


</template>

<script>
    export default {
        name: "Category2Form",
        props: ['categories'],
        data: () => ({
            categoryName: '',
            nameRules: [
                v => !!v || 'Name is required',
            ],
            category: null,
            searchCategory: null,
            index: -1,
            editing: null,
        }),
        watch: {
            category (val, prev) {
                if(val === undefined) return
                if(typeof val === 'string') {
                    this.category = {
                        categoryName: val
                    }
                }
            },
        },

        methods: {
            save () {
                if(!this.$refs.form.validate()) return

                let category = {categoryName: this.categoryName};
                this.$resource('/category{/id}').save({}, category).then(
                    result =>
                        result.json().then(data => {
                            this.categories.push(data)
                            //this.categoryName = ''
                        })
                )

                this.$refs.form.reset()

            },
            clear () {
                this.$refs.form.reset()
            },
            clearCategory(){
                this.category = undefined
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

        },
    }
</script>

<style scoped>

</style>