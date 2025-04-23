import { ref } from "vue";
import { categoriaService } from "@/services/categoria.service";
import type { Categoria } from "@/models/categoria";
import Cookies from "js-cookie";

export function useCategoria() {
    const categorias = ref<Categoria[]>([]);
    const error = ref<string | null>(null);
    const success = ref<string | null>(null);
    const loading = ref<boolean>(false);
    const currentCategoria = ref<Categoria | null>(null);

    const getCategorias = async () => {
        try {
            loading.value = true;
            const response = await categoriaService.getCategorias();
            categorias.value = response.data;
        } catch (err: any) {
            error.value = err.response?.data?.message || "Error fetching categorias";
        } finally {
            loading.value = false;
        }
    };
    const createCategoria = async (categoria: Categoria) => {
        try {
            loading.value = true;
            error.value = null;
            const response = await categoriaService.createCategoria(categoria);
            success.value = "Categoria created successfully!";
            error.value = null;
            await getCategorias();
            return response.data;
        } catch (err: any) {
            console.error("Error creating categoria:", err);
            error.value = err.response?.data?.message || "Error creating categoria";
            success.value = null;
            throw err;
        } finally {
            loading.value = false;
        }
    }
    const getCategoriaById = async (id: string) => {
        try {
            loading.value = true;
            error.value = null;
            const response = await categoriaService.getCategoriaById(id);
            currentCategoria.value = response.data;
        } catch (err: any) {
            console.error("Error fetching categoria by ID:", err);
            error.value = err.response?.data?.message || "Error fetching categoria by ID";
            success.value = null;
            throw err;
        } finally {
            loading.value = false;
        }
    }
    const updateCategoria = async (id: string, categoria: Categoria) => {
        try {
            loading.value = true;
            error.value = null;
            const response = await categoriaService.updateCategoria(id, categoria);
            success.value = "Categoria updated successfully!";
            error.value = null;
            await getCategorias();
            return response.data;
        } catch (err: any) {
            console.error("Error updating categoria:", err);
            error.value = err.response?.data?.message || "Error updating categoria";
            success.value = null;
            throw err;
        } finally {
            loading.value = false;
        }
    }
    const deleteCategoria = async (id: string) => {
        try {
            loading.value = true;
            error.value = null;
            const response = await categoriaService.deleteCategoria(id);
            success.value = "Categoria deleted successfully!";
            error.value = null;
            await getCategorias();
            return response.data;
        } catch (err: any) {
            console.error("Error deleting categoria:", err);
            error.value = err.response?.data?.message || "Error deleting categoria";
            success.value = null;
            throw err;
        } finally {
            loading.value = false;
        }
    }
    return {
        categorias,
        error,
        success,
        loading,
        currentCategoria,
        getCategorias,
        createCategoria,
        getCategoriaById,
        updateCategoria,
        deleteCategoria
    }
}