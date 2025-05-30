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
            console.error("Error obteniendo productos por ID de categoría:", err);
            error.value = err.response?.data?.message || "Error obteniendo productos por ID de categoría";
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
            success.value = "Estado del producto cambiado exitosamente!";
            error.value = null;
            await getProductos();
            return response.data;
        } catch (err: any) {
            console.error("Error cambiando estado del producto:", err);
            error.value = err.response?.data?.message || "Error cambiando estado del producto";
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
            error.value = err.response?.data?.message || "Error obteniendo productos";
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
            console.error("Error obteniendo productos activos:", err);
            error.value = err.response?.data?.message || "Error obteniendo productos activos";
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
            success.value = "Producto creado exitosamente!";
            error.value = null;
            await getProductos();
            return response.data;
        } catch (err: any) {
            console.error("Error creando producto:", err);
            error.value = err.response?.data?.message || "Error creando producto";
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
            console.error("Error obteniendo producto por ID:", err);
            error.value = err.response?.data?.message || "Error obteniendo producto por ID";
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
            success.value = "Producto actualizado exitosamente!";
            error.value = null;
            await getProductos();
            return response.data;
        } catch (err: any) {
            console.error("Error actualizando producto:", err);
            error.value = err.response?.data?.message || "Error actualizando producto";
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
            success.value = "Producto eliminado exitosamente!";
            error.value = null;
            await getProductos();
            return response.data;
        } catch (err: any) {
            console.error("Error eliminando producto:", err);
            error.value = err.response?.data?.message || "Error eliminando producto";
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
            error.value = err.response?.data?.message || "Error obteniendo productos por usuario";
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

            // Obtener el producto actual para conocer el stock actual
            await getProductoById(id);
            if (!currentProducto.value) {
                throw new Error("Producto no encontrado");
            }

            // Asegurar que la cantidad sea válida
            const validQuantity = Math.min(quantity, currentProducto.value.stock);
            const newStock = Math.max(0, currentProducto.value.stock - validQuantity);

            const response = await productoService.markAsSold(id, validQuantity);
            success.value = "Producto marcado como vendido exitosamente!";
            error.value = null;

            // Actualizar el producto actual si coincide
            if (currentProducto.value && currentProducto.value.id === id) {
                currentProducto.value.stock = newStock;
                if (newStock === 0) {
                    currentProducto.value.activo = false;
                }
            }

            // Actualizar la lista de productos si incluye este producto
            const productIndex = productos.value.findIndex(p => p.id === id);
            if (productIndex !== -1) {
                productos.value[productIndex].stock = newStock;
            }

            return response.data;
        } catch (err: any) {
            console.error("Error marcando producto como vendido:", err);
            error.value = err.response?.data?.message || "Error marcando producto como vendido";
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

