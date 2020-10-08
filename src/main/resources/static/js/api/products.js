import Vue from 'vue'

const products = Vue.resource('/product{/id}')

export default {
    add: product => products.save({}, product),
    update: product => products.update({}, product),
    remove: id => products.remove({id: id}),
    getAll: () => products.get(),
}