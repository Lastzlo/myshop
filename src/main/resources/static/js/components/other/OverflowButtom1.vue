<template>
    <v-row align="center">

        <v-container id="dropdown-playground">
            <v-overflow-btn
                    class="my-2"
                    :items="dropdownFont"
                    editable
                    hint="I'm a hint"
                    label="Overflow Btn"
                    target="#dropdown-playground"
            ></v-overflow-btn>
        </v-container>
    </v-row>
</template>

<script>
    export default {
        data: () => ({
            dropdownFont: [
                { text: 'Arial', callback: () => {} },
                { text: 'Calibri', callback: () => {} },
                { text: 'Courier', callback: () => {} },
                { text: 'Verdana', callback: () => {} },
            ],
        }),
    }
</script>

<style scoped>

</style>