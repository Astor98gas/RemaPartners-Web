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

<script lang="ts">
import { defineComponent, ref } from 'vue';
import { useutf8Store } from '@/stores/counter';

export default defineComponent({
    name: 'ImageSelector',
    props: {
        images: {
            type: Array as () => string[],
            required: true
        },
        maxImages: {
            type: Number,
            default: 8
        }
    },
    emits: ['update:images'],
    setup(props, { emit }) {
        const fileInput = ref<HTMLInputElement | null>(null);
        const currentImageIndex = ref(0);
        const imagePreview = ref<string[]>([...props.images]);

        const t = (key: string): string => {
            const store = useutf8Store();
            return store.t(key);
        };

        const triggerFileInput = (index: number) => {
            currentImageIndex.value = index;
            fileInput.value?.click();
        };

        const onFileSelected = (event: Event) => {
            const file = (event.target as HTMLInputElement).files?.[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = (e) => {
                    if (e.target?.result) {
                        const result = e.target.result as string;
                        imagePreview.value[currentImageIndex.value] = result;

                        // Create a new array to trigger the update
                        const updatedImages = [...props.images];
                        updatedImages[currentImageIndex.value] = result;
                        emit('update:images', updatedImages);
                    }
                };
                reader.readAsDataURL(file);
            }
        };

        const removeImage = (index: number) => {
            // Create copies of arrays to avoid reactivity issues
            const newPreview = [...imagePreview.value];
            const newImages = [...props.images];

            // Remove at the specified index
            newPreview.splice(index, 1);
            newPreview.push(''); // Add an empty slot at the end
            imagePreview.value = newPreview;

            newImages.splice(index, 1);
            emit('update:images', newImages);
        };

        return {
            fileInput,
            currentImageIndex,
            imagePreview,
            t,
            triggerFileInput,
            onFileSelected,
            removeImage
        };
    }
});
</script>