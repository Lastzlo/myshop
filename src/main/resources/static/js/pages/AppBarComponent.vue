<template>
    <v-container>
        <v-app-bar app dense>

            <!--        <v-app-bar-nav-icon @click.stop="this.onCatalogDrawer = !this.onCatalogDrawer"></v-app-bar-nav-icon>-->

            <v-btn icon>
                <v-icon>mdi-magnify</v-icon>
            </v-btn>

            <v-spacer></v-spacer>

            <v-btn text>
                My Shop
            </v-btn>

            <v-spacer></v-spacer>

            <v-btn icon @click="openAutorizationDrawer">
                <v-icon>mdi-dots-vertical</v-icon>
            </v-btn>

            <!--        <CatalogDrawerComponent :onCatalogDrawer="onCatalogDrawer"/>-->


        </v-app-bar>

        <AutorizationDrawerComponent
                :changeOnAutorizationDrawer="changeOnAutorizationDrawer"
                :onAutorizationDrawer="onAutorizationDrawer"
        />
    </v-container>
</template>
<script>
    import CatalogDrawerComponent from "pages/CatalogDrawerComponent.vue";
    import AutorizationDrawerComponent from "pages/AutorizationDrawerComponent.vue";

    export default {
        name: 'AppBarComponent',
        components: {CatalogDrawerComponent, AutorizationDrawerComponent},
        // computed: {
        //     onCatalogDrawer: this.$store.getters.catalogDrawer,
        //     onAutorizationDrawer: this.$store.getters.autorizationDrawer
        // }
        data: () => ({
            backupAutorizationDrawer:false,
            onAutorizationDrawer: false,
        }),
        methods: {
            changeOnAutorizationDrawer(change){
                if(change === this.backupAutorizationDrawer){
                    console.log("change === backupAutorizationDrawer")
                }else {
                    console.log("change !== backupAutorizationDrawer")
                    this.backupAutorizationDrawer = change

                }

            },
            openAutorizationDrawer(){
                if(this.backupAutorizationDrawer !==this.onAutorizationDrawer){
                    console.log("this.backupAutorizationDrawer !==this.onAutorizationDrawer")
                    const test = this.backupAutorizationDrawer
                    console.log("const test ="+test)
                    this.onAutorizationDrawer = test

                    this.onAutorizationDrawer = !test
                } else {
                    this.onAutorizationDrawer = true
                    this.backupAutorizationDrawer = true
                }


            },
        },

    }
</script>