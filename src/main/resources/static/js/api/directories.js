import Vue from 'vue'

const directories = Vue.resource('/directory{/id}')

export default {
    add: directory => directories.save({}, directory),
    //update: directory => directories.update({id: directory.id}, directory),
    update: (id, directory) => directories.update({id: id}, directory),
    remove: id => directories.remove({id: id}),
    getCore: () => Vue.resource('/directory/getCore').get(),
}