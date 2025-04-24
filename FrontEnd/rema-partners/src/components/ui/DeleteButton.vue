<template>
    <button class="delete-button">
        <svg viewBox="0 0 448 512" class="delete-icon">
            <path
                d="M135.2 17.7L128 32H32C14.3 32 0 46.3 0 64S14.3 96 32 96H416c17.7 0 32-14.3 32-32s-14.3-32-32-32H320l-7.2-14.3C307.4 6.8 296.3 0 284.2 0H163.8c-12.1 0-23.2 6.8-28.6 17.7zM416 128H32L53.2 467c1.6 25.3 22.6 45 47.9 45H346.9c25.3 0 46.3-19.7 47.9-45L416 128z">
            </path>
        </svg>
        <span class="delete-text">{{ utf8.t('modal.delete') }}</span>
    </button>
</template>

<script lang="ts">
import { useutf8Store } from '@/stores/counter';

export default {
    name: 'DeleteButton',
    setup() {
        const utf8 = useutf8Store(); // Accede al store de traducciones
        return { utf8 };
    }
};
</script>

<!-- Modified from Uiverse.io by vinodjangid07 -->
<style>
.delete-button {
    width: 50px;
    height: 50px;
    border-radius: 10%;
    background-color: rgb(20, 20, 20);
    border: none;
    font-weight: 600;
    display: flex;
    align-items: center;
    justify-content: center;
    box-shadow: 0px 0px 20px rgba(0, 0, 0, 0.164);
    cursor: pointer;
    transition-duration: .3s;
    overflow: hidden;
    position: relative;
}

.delete-icon {
    width: 12px;
    transition-duration: .3s;
}

.delete-icon path {
    fill: white;
}

.delete-button:hover {
    width: 140px;
    border-radius: 10px;
    transition-duration: .3s;
    background-color: rgb(255, 69, 69);
    align-items: center;
}

.delete-button:hover .delete-icon {
    width: 50px;
    transition-duration: .3s;
    transform: translateY(60%);
}

.delete-text {
    position: absolute;
    top: 40%;
    transform: translateY(-50%);
    color: white;
    font-size: 0;
    opacity: 0;
    transition: all 0.3s ease;
}

.delete-button:hover .delete-text {
    font-size: 15px;
    opacity: 1;
}
</style>
