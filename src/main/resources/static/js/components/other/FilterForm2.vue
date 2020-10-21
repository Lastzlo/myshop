<template>
    <div>
        <div id="filterList">
            <v-list>
                <v-list-item
                        v-for="item in items"
                        :key="item.id"
                >
                    <v-list-item-content
                            v-if="item.children.length>0"
                    >
                        <v-list-item-title>{{ item.name }}</v-list-item-title>

                        <v-list-item
                                v-for="child in item.children"
                                :key="child.id"
                        >
                            <div
                                    v-if="child.directoryType ==='BRAND'"
                            >
                                <v-checkbox
                                        v-model="selection2"
                                        :value="child"

                                        :disabled="child.disableStatus === false"
                                >
                                    <template v-slot:label>
                                        <v-list-item-content>
                                            <v-list-item-title>{{ child.name }}</v-list-item-title>
                                        </v-list-item-content>
                                    </template>

                                    <template v-slot:append>
                                        <v-snackbar
                                                v-if="lastSelectedId2!==null && lastSelectedId2===child.id"
                                                :timeout="-1"
                                                :value="true"
                                                absolute
                                                right
                                                top
                                        >
                                            Результаты поиска...
                                        </v-snackbar>
                                    </template>

                                </v-checkbox>
                            </div>


                            <div
                                    v-else-if="child.directoryType ==='PARAMETER'"
                            >

                                <v-list-item-content
                                        v-if="child.children.length>0"
                                >

                                    <v-list-item-title>{{ child.name }}</v-list-item-title>
                                    <v-list-item-title>123456789123456789123456789123456789</v-list-item-title>

                                    <v-list-item
                                            v-for="child2 in child.children"
                                            :key="child2.id"
                                    >

                                        <v-checkbox
                                                v-model="selection2"
                                                :value="child2"

                                                :disabled="child2.disableStatus === false"
                                        >
                                            <template v-slot:label>
                                                <v-list-item-content>
                                                    <v-list-item-title>{{ child2.name }}</v-list-item-title>
                                                </v-list-item-content>
                                            </template>

                                            <template v-slot:append>
                                                <v-snackbar
                                                        v-if="lastSelectedId2!==null && lastSelectedId2===child2.id"
                                                        :timeout="-1"
                                                        :value="true"
                                                        absolute
                                                        right
                                                        top
                                                >
                                                    Результаты поиска...
                                                </v-snackbar>
                                            </template>

                                        </v-checkbox>


                                    </v-list-item>


                                </v-list-item-content>
                            </div>



                        </v-list-item>

                    </v-list-item-content>

                </v-list-item>

                <!--<v-list-item
                        v-for="item in items"
                        :key="item.id"
                >
                    <v-list-group
                            v-if="item.children.length>0"
                            :value="true"
                            sub-group

                    >
                        <template v-slot:activator>

                            <v-list-item-title>{{ item.name }}</v-list-item-title>
                        </template>

                        <template v-slot:default >
                            <v-list-item
                                    v-for="child in item.children"
                                    :key="child.id"

                            >
                                <v-checkbox
                                        v-model="selection2"
                                        :value="child"

                                        :disabled="child.disableStatus === false"
                                >
                                    <template v-slot:label>
                                        <v-list-item-content>
                                            <v-list-item-title>{{ child.name }}</v-list-item-title>
                                        </v-list-item-content>
                                    </template>

                                    <template v-slot:append>
                                        <v-snackbar
                                                v-if="lastSelectedId2!==null && lastSelectedId2===child.id"
                                                :timeout="-1"
                                                :value="true"
                                                absolute
                                                right
                                                top
                                        >
                                            Результаты поиска...
                                        </v-snackbar>
                                    </template>

                                </v-checkbox>

                            </v-list-item>
                        </template>

                    </v-list-group>


                </v-list-item>-->
            </v-list>

            <template v-if="!selection2.length">
                No nodes selected.
            </template>
            <template v-else>
                <div v-for="node in selection2" :key="node.id">
                    {{ node.name }}
                </div>
            </template>
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


            //for listFilter
            selection2: [],
            lastSelectedId2: null,

            disabledList: [],


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

                                item.disableStatus = true
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