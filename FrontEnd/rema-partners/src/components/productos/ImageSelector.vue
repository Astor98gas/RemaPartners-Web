<template>
    <div class="space-y-4">
        <label class="block text-gray-700 font-semibold mb-4 text-lg">
            {{ t('producto.images') }}
        </label>
        <div class="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 gap-4">
            <div v-for="index in maxImages" :key="index"
                class="border-2 border-dashed border-gray-300 rounded-lg p-4 text-center cursor-pointer hover:border-blue-500 transition-colors aspect-square flex flex-col justify-center items-center"
                @click="triggerFileInput(index - 1)">
                <div v-if="imagePreview[index - 1]" class="relative w-full h-full">
                    <img :src="imagePreview[index - 1]" class="w-full h-full object-cover rounded" />
                    <button @click.stop="removeImage(index - 1)"
                        class="absolute top-1 right-1 bg-red-500 text-white rounded-full p-1.5 hover:bg-red-600 shadow-md transition-colors">
                        <span class="sr-only">Remove</span>
                        <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                d="M6 18L18 6M6 6l12 12" />
                        </svg>
                    </button>
                </div>
                <div v-else class="flex flex-col items-center justify-center h-full">
                    <svg class="mx-auto h-12 w-12 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
                    </svg>
                    <span class="mt-2 block text-sm text-gray-600">{{ t('producto.addImage') }}</span>
                </div>
            </div>
        </div>
        <input type="file" ref="fileInput" class="hidden" accept="image/*" @change="onFileSelected" />
    </div>
</template>

<script>
import { useutf8Store } from '@/stores/counter';

export default {
    name: 'ImageSelector',
    props: {
        images: {
            type: Array,
            required: true
        },
        maxImages: {
            type: Number,
            default: 8
        }
    },
    emits: ['update:images'],
    data() {
        return {
            currentImageIndex: 0,
            imagePreview: [...this.images]
        };
    },
    methods: {
        t(key) {
            const store = useutf8Store();
            return store.t(key);
        },
        triggerFileInput(index) {
            this.currentImageIndex = index;
            this.$refs.fileInput.click();
        },
        async onFileSelected(event) {
            const file = event.target.files?.[0];
            if (file) {
                try {
                    // Mostrar preview temporal usando FileReader
                    const reader = new FileReader();
                    reader.onload = (e) => {
                        if (e.target?.result) {
                            this.imagePreview[this.currentImageIndex] = e.target.result;
                        }
                    };
                    reader.readAsDataURL(file);

                    // Crear FormData
                    const formData = new FormData();
                    formData.append('file', file);

                    // Enviar al backend
                    const response = await fetch('http://localhost:8080/api/upload', {
                        method: 'POST',
                        body: formData
                    });

                    if (!response.ok) {
                        throw new Error('Error uploading image');
                    }

                    const data = await response.json();
                    const imageUrl = "http://localhost:8080" + data.url;

                    // Actualizar array de imágenes con la URL
                    const updatedImages = [...this.images];
                    updatedImages[this.currentImageIndex] = imageUrl;
                    this.$emit('update:images', updatedImages);

                } catch (error) {
                    console.error('Error uploading image:', error);
                    // Mostrar error al usuario
                }
            }
        },
        removeImage(index) {
            // Create copies of arrays to avoid reactivity issues
            const newPreview = [...this.imagePreview];
            const newImages = [...this.images];

            // Remove at the specified index
            newPreview.splice(index, 1);
            newPreview.push(''); // Add an empty slot at the end
            this.imagePreview = newPreview;

            newImages.splice(index, 1);
            this.$emit('update:images', newImages);
        }
    },
    watch: {
        images: {
            handler(newImages) {
                this.imagePreview = [...newImages];
            },
            deep: true
        }
    }
};
</script>