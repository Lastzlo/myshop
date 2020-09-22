<template>
    <v-container>
        <v-row>
            <v-col>
                <v-treeview
                        v-model="selection"
                        :items="items"
                        selection-type="leaf"
                        selectable

                        return-object
                >
                    <template v-slot:append="{ item }">

                        <v-icon @click="addChild(item) ">mdi-plus-circle-outline</v-icon>
                        <v-icon v-if="item.directoryType !== 'CORE'" @click="deleteChild(item)">mdi-delete</v-icon>
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
            selection: [],
            items: [
            ],
            defaultDirectory: {
                name: '',
                father: null
            },
        }),
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
                        })
                )

            },
            deleteChild(item) {

                this.$resource('/directory{/id}').remove({id: item.id}).then(result => {
                    if (result.ok) {
                        let father = this.findFatherOfItem(item.id, this.items)
                        if(father.children){
                            let children = father.children
                            children.splice(children.indexOf(item), 1)
                        } else this.items.splice(this.items.indexOf(father), 1)

                    }
                })
            },

            findFatherOfItem(id, items) {
                return items.reduce((acc, item) => {
                    if (acc) {
                        return acc;
                    }

                    if (item.id === id) {
                        return item;

                    }

                    if (item.children) {
                        if(this.findFatherOfItem(id, item.children) != null){
                            return item;
                        }
                    }
                    return acc;
                }, null);
            },
        },
        created: function () {
            //запрос на сервер
            this.$resource('/directory/getCore').get().then(result =>
                result.json().then(data => {
                    this.items.push(data)
                })
            )
        },

    }
</script>

<style scoped>

</style>