<template>
    <!--<v-container>
        <v-dialog v-model="dialog" max-width="500px">
            <v-card>
                <v-card-title>
                    <span class="headline">{{ formTitle }}</span>
                </v-card-title>

                <v-card-text>
                    <v-container>
                        <v-row>
                            <v-col cols="12" sm="6" md="4">
                                <v-text-field
                                        v-model="editedItem.categoryName"
                                        label="Category name"
                                        autofocus
                                ></v-text-field>
                            </v-col>
                        </v-row>
                    </v-container>
                </v-card-text>

                <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn color="blue darken-1" text @click="close">Cancel</v-btn>
                    <v-btn color="blue darken-1" text @click="save">Save</v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>
    </v-container>-->

    <v-container>
        <v-row>
            <v-col>
                <v-treeview
                        v-model="selection"
                        :items="items"
                        :open.sync="open"
                        selection-type="independent"
                        selectable
                        return-object

                >
                    <template v-slot:append="{ item }">

                        <v-icon
                                @click="addChild(item) "
                        >
                            mdi-plus-circle-outline
                        </v-icon>
                        <v-icon
                                @click="editChild(item)"
                        >
                            mdi-pencil
                        </v-icon>
                        <v-icon
                                v-if="item.directoryType !== 'CATEGORY_LIST'"
                                @click="deleteChild(item)"
                        >
                            mdi-delete
                        </v-icon>
                    </template>
                </v-treeview>
            </v-col>
            <v-divider vertical></v-divider>
            <v-col class="pa-6" cols="6">
                <template v-if="!selection.length">
                    No nodes selected.
                </template>
                <template v-else>
                    <div v-for="node in selection" :key="node.id">
                        {{ node.name }}
                    </div>
                </template>
            </v-col>
        </v-row>

    </v-container>


</template>

<script>
    export default {
        name: "DirectoryList",
        data: () => ({
            dialog: false,
            selection: [],
            open: [],
            items: [
            ],
            dialog: false,
            id: -1,
            editedItem: {
                name: '',
                father: null,
                children: []
            },
            defaultItem: {
                name: '',
                father: null,
                children: []
            },
        }),
        computed: {

            formTitle () {
                return this.id === -1 ? 'New Item' : 'Edit Item'
            },
        },
        watch: {
            dialog (val) {
                val || this.close()
            },
        },
        methods: {
            addChild(item) {
                if (!item.children) {
                    this.$set(item, "children", []);

                }

                let linkedDirectory = {
                    name :`${item.name} (${item.children.length})`,
                    father: item,
                    children: []
                }


                this.$resource('/directory{/id}').save({}, linkedDirectory).then(
                    result =>
                        result.json().then(data => {
                            item.children.push(data)

                            //открывает папку
                            this.open.push(item)
                            console.log('this.open = '+this.open)
                        })
                )

            },

            deleteChild(item) {

                this.$resource('/directory{/id}').remove({id: item.id}).then(result => {
                    if (result.ok) {

                        //Удалялет item и его children с this.open
                        this.closeAll2(item)

                        //найти father item
                        let father = this.findFatherOfItem(item.id, this.items)

                        if(father.children){
                            let children = father.children
                            children.splice(children.indexOf(item), 1)
                        } else {
                            this.items.splice(this.items.indexOf(item), 1)
                        }


                    }
                })
            },

            findFatherOfItem(id, items) {
                return items.reduce((acc, item) => {
                    if (acc) {
                        return acc;
                    }

                    if (item.id === id) {
                        let test = {
                            id:-100
                        }
                        return test
                    }

                    if (item.children) {
                        let testId = this.findFatherOfItem(id, item.children)

                        if (testId != null) {
                            if (testId.id===-100) {
                                //console.log("testId.id===-100")
                                return item
                            } else {
                                //console.log("testId.id!=-100")
                                //console.log("testId.name = "+testId.name)
                                return testId
                            }
                        }
                    }

                    return acc;
                }, null)
            },
            //раскрывает все папки
            openAll(items){
                items.forEach(item => {
                    this.open.push(item)
                    if(item.children){
                        this.openAll(item.children)
                    }

                })
            },
            // closeAll(items){
            //     items.forEach(item => {
            //         if(item.children){
            //             this.closeAll(item.children)
            //         }
            //
            //         let index = this.open.findIndex(i => i.id === item.id)
            //         console.log('index = '+index)
            //         if(index>-1){
            //             this.open.splice(index, 1)
            //         }
            //
            //
            //     })
            // },

            closeAll2(item){
                if(item.children){
                    item.children.forEach(item => {
                        this.closeAll2(item)
                    })
                }

                let index = this.open.findIndex(i => i.id === item.id)
                //console.log('index = '+index)
                if(index>-1){
                    this.open.splice(index, 1)
                }

            }

        },
        created: function () {
            //запрос на сервер
            this.$resource('/directory/getCore').get().then(result =>
                result.json().then(data => {
                    //this.items = data.children

                    this.items.push(data)

                    this.openAll(this.items)
                })
            )
        },

    }
</script>

<style scoped>

</style>