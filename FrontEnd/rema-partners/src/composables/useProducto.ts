import { ref } from "vue";
import { productoService } from "@/services/producto.service";
import type { Producto, ProductoModify } from "@/models/producto";
import Cookies from "js-cookie";

/**
 * Composable para gestionar productos.
 * Permite obtener, crear, actualizar, eliminar productos y operaciones relacionadas.
 */
export function useProducto() {
    const productos = ref<Producto[]>([]);
    const error = ref<string | null>(null);
    const success = ref<string | null>(null);
    const loading = ref<boolean>(false);
    const currentProducto = ref<Producto | null>(null);

    /**
     * Obtiene productos por ID de categoría.
     * @param {string} id - ID de la categoría.
     * @returns {Promise<void>}
     */
    const getProductosByIdCategoria = async (id: string) => {
        try {
            loading.value = true;
            error.value = null;
            const response = await productoService.getProductosByIdCategoria(id);
            productos.value = response.data;
        } catch (err: any) {
            console.error("Error fetching productos by category ID:", err);
            error.value = err.response?.data?.message || "Error fetching productos by category ID";
            success.value = null;
            throw err;
        } finally {
            loading.value = false;
        }
    };

    /**
     * Cambia el estado (activo/inactivo) de un producto.
     * @param {string} id - ID del producto.
     * @returns {Promise<any>}
     */
    const toggleStatus = async (id: string) => {
        try {
            loading.value = true;
            error.value = null;
            const response = await productoService.toggleStatus(id);
            success.value = "Producto status toggled successfully!";
            error.value = null;
            await getProductos();
            return response.data;
        } catch (err: any) {
            console.error("Error toggling producto status:", err);
            error.value = err.response?.data?.message || "Error toggling producto status";
            success.value = null;
            throw err;
        } finally {
            loading.value = false;
        }
    };

    /**
     * Obtiene todos los productos.
     * @returns {Promise<void>}
     */
    const getProductos = async () => {
        try {
            loading.value = true;
            const response = await productoService.getProductos();
            productos.value = response.data;
        } catch (err: any) {
            error.value = err.response?.data?.message || "Error fetching productos";
        } finally {
            loading.value = false;
        }
    };
    /**
     * Obtiene solo los productos activos.
     * @returns {Promise<void>}
     */
    const getProductosActivos = async () => {
        try {
            loading.value = true;
            error.value = null;
            const response = await productoService.getProductos();
            productos.value = response.data.filter((producto: Producto) => producto.activo);
        } catch (err: any) {
            console.error("Error fetching active productos:", err);
            error.value = err.response?.data?.message || "Error fetching active productos";
            success.value = null;
            throw err;
        } finally {
            loading.value = false;
        }
    }
    /**
     * Crea un nuevo producto.
     * @param {ProductoModify} producto - Datos del producto.
     * @returns {Promise<any>}
     */
    const createProducto = async (producto: ProductoModify) => {
        try {
            loading.value = true;
            error.value = null;
            const response = await productoService.createProducto(producto);
            success.value = "Producto created successfully!";
            error.value = null;
            await getProductos();
            return response.data;
        } catch (err: any) {
            console.error("Error creating producto:", err);
            error.value = err.response?.data?.message || "Error creating producto";
            success.value = null;
            throw err;
        } finally {
            loading.value = false;
        }
    }
    /**
     * Obtiene un producto por su ID.
     * @param {string} id - ID del producto.
     * @returns {Promise<any>}
     */
    const getProductoById = async (id: string) => {
        try {
            loading.value = true;
            error.value = null;
            const response = await productoService.getProductoById(id);
            currentProducto.value = response.data;
        } catch (err: any) {
            console.error("Error fetching producto by ID:", err);
            error.value = err.response?.data?.message || "Error fetching producto by ID";
            success.value = null;
            throw err;
        } finally {
            loading.value = false;
        }
    }
    /**
     * Actualiza un producto existente.
     * @param {string} id - ID del producto.
     * @param {ProductoModify} producto - Datos a actualizar.
     * @returns {Promise<any>}
     */
    const updateProducto = async (id: string, producto: ProductoModify) => {
        try {
            loading.value = true;
            error.value = null;
            const response = await productoService.updateProducto(id, producto);
            success.value = "Producto updated successfully!";
            error.value = null;
            await getProductos();
            return response.data;
        } catch (err: any) {
            console.error("Error updating producto:", err);
            error.value = err.response?.data?.message || "Error updating producto";
            success.value = null;
            throw err;
        } finally {
            loading.value = false;
        }
    }
    /**
     * Elimina un producto por su ID.
     * @param {string} id - ID del producto.
     * @returns {Promise<any>}
     */
    const deleteProducto = async (id: string) => {
        try {
            loading.value = true;
            error.value = null;
            const response = await productoService.deleteProducto(id);
            success.value = "Producto deleted successfully!";
            error.value = null;
            await getProductos();
            return response.data;
        } catch (err: any) {
            console.error("Error deleting producto:", err);
            error.value = err.response?.data?.message || "Error deleting producto";
            success.value = null;
            throw err;
        } finally {
            loading.value = false;
        }
    }
    /**
     * Obtiene productos por ID de usuario.
     * @param {string} idUsuario - ID del usuario.
     * @returns {Promise<void>}
     */
    const getProductosByUsuario = async (idUsuario: string) => {
        try {
            loading.value = true;
            error.value = null;
            const response = await productoService.getProductosByUsuario(idUsuario);
            productos.value = response.data;
        } catch (err: any) {
            error.value = err.response?.data?.message || "Error fetching productos by user";
        } finally {
            loading.value = false;
        }
    }
    /**
     * Marca un producto como vendido y actualiza el stock.
     * @param {string} id - ID del producto.
     * @param {number} quantity - Cantidad vendida.
     * @returns {Promise<any>}
     */
    const markAsSold = async (id: string, quantity: number) => {
        try {
            loading.value = true;
            error.value = null;

            // Get current product to know current stock
            await getProductoById(id);
            if (!currentProducto.value) {
                throw new Error("Product not found");
            }

            // Ensure quantity is valid
            const validQuantity = Math.min(quantity, currentProducto.value.stock);
            const newStock = Math.max(0, currentProducto.value.stock - validQuantity);

            const response = await productoService.markAsSold(id, validQuantity);
            success.value = "Product marked as sold successfully!";
            error.value = null;

            // Update the current product if it matches
            if (currentProducto.value && currentProducto.value.id === id) {
                currentProducto.value.stock = newStock;
            }

            // Update products list if it includes this product
            const productIndex = productos.value.findIndex(p => p.id === id);
            if (productIndex !== -1) {
                productos.value[productIndex].stock = newStock;
            }

            return response.data;
        } catch (err: any) {
            console.error("Error marking product as sold:", err);
            error.value = err.response?.data?.message || "Error marking product as sold";
            success.value = null;
            throw err;
        } finally {
            loading.value = false;
        }
    };

    return {
        productos,
        error,
        success,
        loading,
        currentProducto,
        getProductos,
        createProducto,
        getProductoById,
        updateProducto,
        deleteProducto,
        getProductosByUsuario,
        toggleStatus,
        getProductosActivos,
        getProductosByIdCategoria,
        markAsSold
    }

}

