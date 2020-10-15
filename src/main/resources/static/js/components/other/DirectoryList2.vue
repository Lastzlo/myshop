<template>
    <div>
        <div>
            <v-treeview
                    v-model="selection"
                    hoverable
                    open-on-click
                    :items="items"
                    :open.sync="open"
                    selection-type="independent"
                    selectable
                    return-object
            >
            </v-treeview>
            <template v-if="!selection.length">
                No nodes selected.
            </template>
            <template v-else>
                <div v-for="node in selection" :key="node.id">
                    {{ node.name }}
                </div>
            </template>
        </div>
    </div>


</template>

<script>
    //используем апи
    import directoriesApi from 'api/directories'

    export default {
        name: "DirectoryList",
        props:[
            'tegsFromProduct',
        ],
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
                val || this.closeDialog()
            },
            selection() {
                this.$emit("selected-tags", this.selection);
            },

            tegsFromProduct(newVal, oldVal){
                this.selection = newVal;
            }
        },
        methods: {
            closeDialog () {
                this.dialog = false
                this.$nextTick(() => {
                    this.editedItem = Object.assign({}, this.defaultItem)
                    this.id = -1
                })
            },
            addChild(item) {
                this.editedItem.father = item
                this.dialog = true
            },
            editChild(item){
                this.id = item.id
                this.editedItem = Object.assign({}, item)
                this.dialog = true
            },
            save(){
                let linkedDirectory = this.editedItem

                if (this.id > -1) {
                    //update
                    directoriesApi.update(this.id, linkedDirectory).then(result =>
                        result.json().then(data => {
                            let father = this.findFatherOfItem(data.id, this.items)
                            let index = father.children.findIndex(item => item.id === data.id)
                            father.children.splice(index, 1, data)

                        })
                    )

                } else {
                    //save
                    let father = this.editedItem.father
                    directoriesApi.add(linkedDirectory).then(
                        result =>
                            result.json().then(data => {
                                father.children.push(data)
                                //открывает папку
                                this.open.push(father)

                                this.openAll(father.children)
                            })
                    )
                }

                this.closeDialog()
            },

            deleteChild(item) {

                directoriesApi.remove(item.id).then(result => {
                    if (result.ok) {

                        //Удалялет item и его children с this.open
                        this.closeAll(item)

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
                                return item
                            } else {
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
            closeAll(item){
                if(item.children){
                    item.children.forEach(item => {
                        this.closeAll(item)
                    })
                }

                let index = this.open.findIndex(i => i.id === item.id)
                //console.log('index = '+index)
                if(index>-1){
                    this.open.splice(index, 1)
                }
            },
        },
        created: function () {

            if(this.$route.params.id !==null){
                let directoryId = this.$route.params.id

                //запрос на сервер
                directoriesApi.getOne(directoryId).then(result =>
                    result.json().then(data => {
                        this.items.push(data)

                        this.openAll(this.items)
                    })
                )
            } /*else {
                //запрос на сервер
                directoriesApi.getCore().then(result =>
                    result.json().then(data => {
                        this.items.push(data)

                        this.openAll(this.items)
                    })
                )
            }*/
        },
    }
</script>

<style scoped>

</style>