<template>
    <v-app-bar app dense>

        <v-app-bar-nav-icon @click.stop="onCatalogDrawer = !onCatalogDrawer"></v-app-bar-nav-icon>

        <v-btn icon>
            <v-icon>mdi-magnify</v-icon>
        </v-btn>

        <v-spacer></v-spacer>

        <v-btn text>
            My Shop
        </v-btn>

        <v-spacer></v-spacer>

        <v-btn icon @click.stop="onAutorizationDrawer = !onAutorizationDrawer">
            <v-icon>mdi-dots-vertical</v-icon>
        </v-btn>

        <CatalogDrawerComponent :onCatalogDrawer="onCatalogDrawer"/>

        <AutorizationDrawerComponent :onAutorizationDrawer="onAutorizationDrawer"/>
    </v-app-bar>
</template>
<script>
    import CatalogDrawerComponent from "pages/CatalogDrawerComponent.vue";
    import AutorizationDrawerComponent from "pages/AutorizationDrawerComponent.vue";

    export default {
        name: 'AppBarComponent',
        components: {CatalogDrawerComponent, AutorizationDrawerComponent},
        data: () => ({
            onCatalogDrawer: false,
            onAutorizationDrawer: false,
        }),
    }
</script>