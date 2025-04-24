<template>
    <button class="edit-button">
        <svg viewBox="0 0 24 24" class="edit-icon" xmlns="http://www.w3.org/2000/svg">
            <g id="SVGRepo_iconCarrier">
                <path
                    d="M21.2799 6.40005L11.7399 15.94C10.7899 16.89 7.96987 17.33 7.33987 16.7C6.70987 16.07 7.13987 13.25 8.08987 12.3L17.6399 2.75002C17.8754 2.49308 18.1605 2.28654 18.4781 2.14284C18.7956 1.99914 19.139 1.92124 19.4875 1.9139C19.8359 1.90657 20.1823 1.96991 20.5056 2.10012C20.8289 2.23033 21.1225 2.42473 21.3686 2.67153C21.6147 2.91833 21.8083 3.21243 21.9376 3.53609C22.0669 3.85976 22.1294 4.20626 22.1211 4.55471C22.1128 4.90316 22.0339 5.24635 21.8894 5.5635C21.7448 5.88065 21.5375 6.16524 21.2799 6.40005V6.40005Z"
                    stroke="#ffffff" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path>
                <path
                    d="M11 4H6C4.93913 4 3.92178 4.42142 3.17163 5.17157C2.42149 5.92172 2 6.93913 2 8V18C2 19.0609 2.42149 20.0783 3.17163 20.8284C3.92178 21.5786 4.93913 22 6 22H17C19.21 22 20 20.2 20 18V13"
                    stroke="#ffffff" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path>
            </g>
        </svg>
        <span class="edit-text">{{ utf8.t('modal.edit') }}</span>
    </button>
</template>

<script lang="ts">
import { useutf8Store } from '@/stores/counter';

export default {
    name: 'EditButton',
    setup() {
        const utf8 = useutf8Store(); // Accede al store de traducciones
        return { utf8 };
    }
};
</script>

<!-- Modified from Uiverse.io by vinodjangid07 -->
<style>
.edit-button {
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

.edit-icon {
    width: 20px;
    transition-duration: .3s;
}

.edit-icon path {
    fill: none;
}

.edit-button:hover {
    width: 140px;
    border-radius: 10px;
    transition-duration: .3s;
    background-color: rgb(127, 179, 213);
    align-items: center;
}

.edit-button:hover .edit-icon {
    width: 50px;
    transition-duration: .3s;
    transform: translateY(60%);
}

.edit-text {
    position: absolute;
    top: 40%;
    transform: translateY(-50%);
    color: white;
    font-size: 0;
    opacity: 0;
    transition: all 0.3s ease;
}

.edit-button:hover .edit-text {
    font-size: 15px;
    opacity: 1;
}
</style>
