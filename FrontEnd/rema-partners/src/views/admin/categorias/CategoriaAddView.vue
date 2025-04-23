<template>

</template>

<script lang="ts">

export default {
    name: 'CategoriaAddView',
    data() {
        return {
            newCategoria: {
                titulo: '',
                descripcion: '',
                campos: []
            }
        }
    },
    methods: {
        addCategoria() {
            // Aquí puedes agregar la lógica para enviar la nueva categoría al servidor
            console.log('Nueva categoría:', this.newCategoria);
        }
    }
}

</script>