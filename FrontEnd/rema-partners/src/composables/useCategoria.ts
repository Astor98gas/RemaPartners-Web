import { ref } from "vue";
import { categoriaService } from "@/services/categoria.service";
import type { Categoria, CategoriaModify } from "@/models/categoria";
import Cookies from "js-cookie";

/**
 * Composable para gestionar las operaciones relacionadas con categorías.
 * Proporciona métodos para obtener, crear, actualizar y eliminar categorías.
 * También maneja estados de carga, éxito y error.
 */
export function useCategoria() {
    const categorias = ref<Categoria[]>([]);
    const error = ref<string | null>(null);
    const success = ref<string | null>(null);
    const loading = ref<boolean>(false);
    const currentCategoria = ref<Categoria | null>(null);

    /**
     * Obtiene todas las categorías.
     * @returns {Promise<void>}
     */
    const getCategorias = async () => {
        try {
            loading.value = true;
            const response = await categoriaService.getCategorias();
            categorias.value = response.data;
        } catch (err: any) {
            error.value = err.response?.data?.message || "Error obteniendo categorías";
        } finally {
            loading.value = false;
        }
    };
    /**
     * Crea una nueva categoría.
     * @param {CategoriaModify} categoria - Datos de la categoría a crear.
     * @returns {Promise<any>} - Categoría creada.
     * @throws Error si ocurre un problema al crear.
     */
    const createCategoria = async (categoria: CategoriaModify) => {
        try {
            loading.value = true;
            error.value = null;
            const response = await categoriaService.createCategoria(categoria);
            success.value = "Categoría creada exitosamente!";
            error.value = null;
            await getCategorias();
            return response.data;
        } catch (err: any) {
            console.error("Error creando categoría:", err);
            error.value = err.response?.data?.message || "Error creando categoría";
            success.value = null;
            throw err;
        } finally {
            loading.value = false;
        }
    }
    /**
     * Obtiene una categoría por su ID.
     * @param {string} id - ID de la categoría.
     * @returns {Promise<any>} - Categoría encontrada.
     * @throws Error si ocurre un problema al buscar.
     */
    const getCategoriaById = async (id: string) => {
        try {
            loading.value = true;
            error.value = null;
            const response = await categoriaService.getCategoriaById(id);
            currentCategoria.value = response.data;
        } catch (err: any) {
            console.error("Error obteniendo categoría por ID:", err);
            error.value = err.response?.data?.message || "Error obteniendo categoría por ID";
            success.value = null;
            throw err;
        } finally {
            loading.value = false;
        }
    }
    /**
     * Actualiza una categoría existente.
     * @param {string} id - ID de la categoría.
     * @param {CategoriaModify} categoria - Datos a actualizar.
     * @returns {Promise<any>} - Categoría actualizada.
     * @throws Error si ocurre un problema al actualizar.
     */
    const updateCategoria = async (id: string, categoria: CategoriaModify) => {
        try {
            loading.value = true;
            error.value = null;
            const response = await categoriaService.updateCategoria(id, categoria);
            success.value = "Categoría actualizada exitosamente!";
            error.value = null;
            await getCategorias();
            return response.data;
        } catch (err: any) {
            console.error("Error actualizando categoría:", err);
            error.value = err.response?.data?.message || "Error actualizando categoría";
            success.value = null;
            throw err;
        } finally {
            loading.value = false;
        }
    }
    /**
     * Elimina una categoría por su ID.
     * @param {string} id - ID de la categoría.
     * @returns {Promise<any>} - Respuesta de eliminación.
     * @throws Error si ocurre un problema al eliminar.
     */
    const deleteCategoria = async (id: string) => {
        try {
            loading.value = true;
            error.value = null;
            const response = await categoriaService.deleteCategoria(id);
            success.value = "Categoría eliminada exitosamente!";
            error.value = null;
            await getCategorias();
            return response.data;
        } catch (err: any) {
            console.error("Error eliminando categoría:", err);
            error.value = err.response?.data?.message || "Error eliminando categoría";
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