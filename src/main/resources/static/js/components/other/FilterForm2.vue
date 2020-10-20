<template>
    <div>
        <div id="filterList">
            <v-list>
                <v-list-item
                        v-for="item in items"
                        :key="item.id"
                >
                    <v-list-item-content v-if="item.children.length>0">
                        <v-list-item-title>{{ item.name }}</v-list-item-title>

                        <v-list-item
                                v-for="child in item.children"
                                :key="child.id"
                        >
                            <v-checkbox
                                    v-model="selection2"
                                    :value="child"
                            >
                                <template v-slot:label>
                                    <v-list-item-content>
                                        <v-list-item-title>{{ child.name }}</v-list-item-title>
                                        <v-list-item-subtitle v-if="lastSelectedId2!==null && lastSelectedId2===child.id">Последний отмеченый</v-list-item-subtitle>

                                    </v-list-item-content>
                                </template>

                            </v-checkbox>

                        </v-list-item>

                    </v-list-item-content>
                </v-list-item>
            </v-list>
        </div>


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
                <template v-slot:append="{item}">

                    <div>
                        <v-snackbar
                                v-if="lastSelectedId!==null && lastSelectedId===item.id"
                                :timeout="-1"
                                :value="true"
                                absolute

                        >
                            Результаты поиска...
                        </v-snackbar>
                    </div>

                    <!--<div id="snackbar">
                        <v-snackbar
                                v-model="item.snackbarOpen"

                                absolute

                        >
                            {{ text }}

                            <v-btn
                                    color="pink"

                                    @click="this.snackbarClose(item)"
                            >
                                Close
                            </v-btn>
                        </v-snackbar>
                    </div>-->

                </template>
            </v-treeview>
            <template v-if="!selection.length">
                No nodes selected.
            </template>
            <template v-else>
                <div v-for="node in selection" :key="node.id">
                    {{ node.name }}
                </div>
            </template>


            <!--<div class="text-center">

                <v-menu
                        top
                        offset-x="true"
                >
                    <template v-slot:activator="{ on, attrs }">
                        <v-btn
                                color="primary"
                                dark
                                v-bind="attrs"
                                v-on="on"
                        >
                            Dropdown
                        </v-btn>
                    </template>

                    <v-list>
                        <v-list-item
                                v-for="(item, index) in items2"
                                :key="index"
                        >
                            <v-list-item-title>{{ item.title }}</v-list-item-title>
                        </v-list-item>
                    </v-list>
                </v-menu>
            </div>-->

        </div>
    </div>


</template>

<script>
    //используем апи
    import directoriesApi from 'api/directories'
    import {mapState, mapMutations} from 'vuex'

    export default {
        name: "FilterForm2",
        data: () => ({
            selection: [],
            open: [],

            items: [],
            oldLastTag: null,
            lastSelectedId: null,
            text: 'Hello, I\'m a snackbar',
            //for menu component
            // items2: [
            //     { title: 'Click Me' },
            // ],

            //for listFilter
            selection2: [],
            lastSelectedId2: null


        }),
        computed: {
            ...mapState(['tagsForFilterForm']),
        },
        watch: {
            selection(newVal, oldVal) {
                if(newVal!==oldVal){
                    this.setTagsForFilterForm(this.selection)

                    if(newVal.length>0){

                        //показать окно возле последнего элемента массива
                        let lastTag = newVal[newVal.length-1]
                        console.log("последний элемент массива это = "+lastTag.name)

                        this.lastSelectedId = lastTag.id

                        // if(this.oldLastTag !==null){
                        //     this.closeOldLastTag(this.oldLastTag)
                        // }
                        // this.oldLastTag = lastTag
                        //
                        // this.openLastTag(lastTag)
                    }
                }
            },
            selection2(newVal, oldVal) {
                if(newVal!==oldVal){
                    //this.setTagsForFilterForm(this.selection)

                    if(newVal.length>0){

                        //показать окно возле последнего элемента массива
                        let lastTag = newVal[newVal.length-1]
                        console.log("последний элемент массива2 это = "+lastTag.name)

                        //установили this.lastSelectedId2
                        this.lastSelectedId2 = lastTag.id

                        // if(this.oldLastTag !==null){
                        //     this.closeOldLastTag(this.oldLastTag)
                        // }
                        // this.oldLastTag = lastTag
                        //
                        // this.openLastTag(lastTag)
                    }
                }
            },
            $route(to, from) {
                // обрабатываем изменение параметров маршрута...
                let directoryId = to.params.id
                //console.log("newDirectoryId = "+this.directoryId)
                this.setItems(directoryId)
            }
        },
        methods: {
            ...mapMutations(['setTagsForFilterForm']),
            //раскрывает все папки
            openAll(items){
                items.forEach(item => {
                    this.open.push(item)
                    if(item.children){
                        this.openAll(item.children)
                    }

                })
            },
            openLastTag(lastTag){
                console.log("openLastTag")
                console.log("lastTag.name = "+lastTag.name)
                lastTag.snackbarOpen = true
            },
            snackbarClose(item){
                console.log("snackbarClose ")
                console.log("item.name = "+item.name)
                item.snackbarOpen = false
            },
            closeOldLastTag(oldLastTag){
                console.log("closeOldLastTag")
                console.log("oldLastTag.name = "+oldLastTag.name)
            },
            setItems(directoryId){

                this.items = []
                //запрос на сервер
                directoriesApi.getOne(directoryId).then(result =>
                    result.json().then(data => {
                        data.children.forEach(
                            item => {
                                //добавили значение isOpened
                                item.snackbarOpen = false

                                //obj.param125 = '123';


                                this.items.push(item)
                            }
                        )

                        this.openAll(this.items)






                    })
                )
            },
        },
        created: function () {
            if(this.$route.params.id !==null){
                let directoryId = this.$route.params.id

                this.setItems(directoryId)
            }
        },
    }
</script>

<style scoped>

</style>