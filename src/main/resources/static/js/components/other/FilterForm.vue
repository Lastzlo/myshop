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
                <template v-slot:append="{item}">

                    <div id="snackbar">
                        <v-snackbar
                                v-model="item.snackbarOpen"
                                :timeout="item.snackbarTimeout"
                                absolute

                        >
                            {{ text }}

                            <template v-slot:action="{ attrs }">
                                <v-btn
                                        color="pink"
                                        text
                                        v-bind="attrs"
                                        @click="item.snackbarOpen = false"
                                >
                                    Close
                                </v-btn>
                            </template>
                        </v-snackbar>
                    </div>

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



            <!--<v-card
                    height="300"
                    flat
            >
                <v-btn
                        dark
                        @click="snackbar = true"
                >
                    Open Snackbar
                </v-btn>
                <v-snackbar
                        v-model="snackbar"
                        absolute
                        left
                        top
                >
                    {{ text }}

                    <template v-slot:action="{ attrs }">
                        <v-btn
                                color="pink"
                                text
                                v-bind="attrs"
                                @click="snackbar = false"
                        >
                            Close
                        </v-btn>
                    </template>
                </v-snackbar>
            </v-card>-->

        </div>
    </div>


</template>

<script>
    //используем апи
    import directoriesApi from 'api/directories'
    import {mapState, mapMutations} from 'vuex'

    export default {
        name: "FilterForm",
        data: () => ({
            selection: [],
            open: [],
            items: [],
            snackbar: false,
            oldLastTag: null,
            text: 'Hello, I\'m a snackbar',
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

                        if(this.oldLastTag !==null){
                            this.closeOldLastTag(this.oldLastTag)
                        }
                        this.oldLastTag = lastTag

                        this.openLastTag(lastTag)
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

                lastTag.snackbarTimeout = -1
                lastTag.snackbarOpen = true

            },
            closeOldLastTag(oldLastTag){
                console.log("closeOldLastTag")
                console.log("oldLastTag.name = "+oldLastTag.name)

                oldLastTag.snackbarTimeout = 100
                //oldLastTag.snackbarOpen = false


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
                                item.snackbarTimeout = 100
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