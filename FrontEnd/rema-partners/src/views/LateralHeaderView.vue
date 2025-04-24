<template>
    <div class="flex flex-col h-full sticky top-0">
        <!-- Sidebar -->
        <aside class="bg-gray-800 text-white w-64 flex flex-col h-full">
            <header class="p-4">
                <h1 class="text-2xl font-bold">Lateral Header</h1>
            </header>
            <nav class="flex-1 p-4 overflow-y-auto">
                <ul>
                    <li class="mb-2">
                        <a href="#" class="text-white hover:underline">Link 1</a>
                    </li>
                    <li class="mb-2">
                        <a href="#" class="text-white hover:underline">Link 2</a>
                    </li>
                    <li class="mb-2">
                        <a href="#" class="text-white hover:underline">Link 3</a>
                    </li>
                </ul>
            </nav>
            <footer class="p-4">
                <p>&copy; 2023 Your Company</p>
            </footer>
        </aside>
    </div>
</template>

<script lang="ts">
export default {
    name: 'LateralHeaderView'
};
</script>