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
    import {mapState, mapMutations} from 'vuex'

    export default {
        name: "FilterForm",
        data: () => ({
            selection: [],
            open: [],
            items: [],
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
                        console.log("последний элемент массива это = "+newVal[newVal.length-1].name)
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
            setItems(directoryId){

                this.items = []
                //запрос на сервер
                directoriesApi.getOne(directoryId).then(result =>
                    result.json().then(data => {
                        data.children.forEach(
                            item => {
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