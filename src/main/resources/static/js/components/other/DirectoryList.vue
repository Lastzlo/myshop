<template>
    <v-container>
        <v-row>
            <v-col>
                <v-treeview
                        v-model="selection"

                        :items="directories"
                        item-key="name"
                        selection-type="leaf"
                        selectable

                        activatable
                        open-all


                        return-object
                >
                    <template v-slot:append="{ item, open }">
                        <v-btn @click="addChild(item)">Add child</v-btn>
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
            directories: [
                /*{
                    id: 1,
                    name: 'CategoryList',
                    children: [
                        { id: 7, name: 'Grandchild #3' },
                        { id: 8, name: 'Grandchild #4' },
                    ],
                },
                {
                    id: 2,
                    name: 'Root2',
                    children: [
                        { id: 5, name: 'Grandchild #1' },
                        { id: 6, name: 'Grandchild #2' },
                    ],
                },*/
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


        },
        created: function () {
            //запрос на сервер
            this.$resource('/directory/getCore').get().then(result =>
                result.json().then(data => {
                    this.directories.push(data)
                })
            )
        },

    }
</script>

<style scoped>

</style>