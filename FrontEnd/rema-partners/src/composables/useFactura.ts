import { ref } from "vue";
import { facturaService } from "@/services/factura.service";
import type { FacturaEntity } from "@/models/factura";

/**
 * Composable para gestionar facturas.
 * Permite crear, obtener y listar facturas por diferentes criterios.
 */
export function useFactura() {
    const currentFactura = ref<FacturaEntity | null>(null);
    const facturas = ref<FacturaEntity[]>([]);
    const error = ref<string | null>(null);
    const success = ref<string | null>(null);
    const loading = ref<boolean>(false);

    /**
     * Crea una nueva factura.
     * @param {FacturaEntity} factura - Datos de la factura.
     * @returns {Promise<any>} Factura creada.
     * @throws Error si ocurre un problema al crear.
     */
    const createFactura = async (factura: FacturaEntity) => {
        try {
            loading.value = true;
            error.value = null;
            const response = await facturaService.createFactura(factura);
            currentFactura.value = response.data;
            success.value = "Factura creada exitosamente";
            return response.data;
        } catch (err: any) {
            console.error("Error creando factura:", err);
            error.value = err.response?.data?.message || "Error creando factura";
            success.value = null;
            throw err;
        } finally {
            loading.value = false;
        }
    };

    /**
     * Crea una factura a partir de una venta.
     * @param {string} idProducto - ID del producto vendido.
     * @param {string} idComprador - ID del comprador.
     * @param {string} idVendedor - ID del vendedor.
     * @param {number} cantidad - Cantidad vendida.
     * @param {string} idChat - ID del chat asociado.
     * @returns {Promise<any>} Factura creada.
     * @throws Error si ocurre un problema al crear.
     */
    const createFromSale = async (idProducto: string, idComprador: string, idVendedor: string, cantidad: number, idChat: string) => {
        try {
            loading.value = true;
            error.value = null;
            const response = await facturaService.createFromSale(idProducto, idComprador, idVendedor, cantidad, idChat);
            currentFactura.value = response.data;
            success.value = "Factura creada exitosamente desde la venta";
            return response.data;
        } catch (err: any) {
            console.error("Error creando factura desde la venta:", err);
            error.value = err.response?.data?.message || "Error creando factura desde la venta";
            success.value = null;
            throw err;
        } finally {
            loading.value = false;
        }
    };

    /**
     * Obtiene una factura por su ID.
     * @param {string} id - ID de la factura.
     * @returns {Promise<any>} Factura encontrada.
     * @throws Error si ocurre un problema al buscar.
     */
    const getFacturaById = async (id: string) => {
        try {
            loading.value = true;
            error.value = null;
            const response = await facturaService.getFacturaById(id);
            currentFactura.value = response.data;
            return response.data;
        } catch (err: any) {
            console.error("Error obteniendo factura por ID:", err);
            error.value = err.response?.data?.message || "Error obteniendo factura por ID";
            success.value = null;
            throw err;
        } finally {
            loading.value = false;
        }
    };

    /**
     * Obtiene todas las facturas donde el usuario es comprador.
     * @param {string} idComprador - ID del comprador.
     * @returns {Promise<any[]>} Lista de facturas.
     * @throws Error si ocurre un problema al buscar.
     */
    const getFacturasByBuyerId = async (idComprador: string) => {
        try {
            loading.value = true;
            error.value = null;
            console.log(`useFactura: Obteniendo facturas para comprador con ID: ${idComprador}`);
            const response = await facturaService.getFacturasByBuyerId(idComprador);
            console.log("useFactura: Respuesta de API de facturas de comprador:", response);
            facturas.value = response.data;
            return response.data;
        } catch (err: any) {
            console.error("Error obteniendo facturas de comprador:", err);
            error.value = err.response?.data?.message || "Error obteniendo facturas de comprador";
            success.value = null;
            throw err;
        } finally {
            loading.value = false;
        }
    };

    /**
     * Obtiene todas las facturas donde el usuario es vendedor.
     * @param {string} idVendedor - ID del vendedor.
     * @returns {Promise<any[]>} Lista de facturas.
     * @throws Error si ocurre un problema al buscar.
     */
    const getFacturasBySellerId = async (idVendedor: string) => {
        try {
            loading.value = true;
            error.value = null;
            console.log(`useFactura: Obteniendo facturas para vendedor con ID: ${idVendedor}`);
            const response = await facturaService.getFacturasBySellerId(idVendedor);
            console.log("useFactura: Respuesta de API de facturas de vendedor:", response);
            facturas.value = response.data;
            return response.data;
        } catch (err: any) {
            console.error("Error obteniendo facturas de vendedor:", err);
            error.value = err.response?.data?.message || "Error obteniendo facturas de vendedor";
            success.value = null;
            throw err;
        } finally {
            loading.value = false;
        }
    };

    /**
     * Obtiene todas las facturas asociadas a un producto.
     * @param {string} idProducto - ID del producto.
     * @returns {Promise<any[]>} Lista de facturas.
     * @throws Error si ocurre un problema al buscar.
     */
    const getFacturasByProductId = async (idProducto: string) => {
        try {
            loading.value = true;
            error.value = null;
            const response = await facturaService.getFacturasByProductId(idProducto);
            facturas.value = response.data;
            return response.data;
        } catch (err: any) {
            console.error("Error obteniendo facturas de producto:", err);
            error.value = err.response?.data?.message || "Error obteniendo facturas de producto";
            success.value = null;
            throw err;
        } finally {
            loading.value = false;
        }
    };

    /**
     * Obtiene todas las facturas asociadas a un chat.
     * @param {string} idChat - ID del chat.
     * @returns {Promise<any[]>} Lista de facturas.
     * @throws Error si ocurre un problema al buscar.
     */
    const getFacturasByChatId = async (idChat: string) => {
        try {
            loading.value = true;
            error.value = null;
            const response = await facturaService.getFacturasByChatId(idChat);
            facturas.value = response.data;
            return response.data;
        } catch (err: any) {
            console.error("Error obteniendo facturas de chat:", err);
            error.value = err.response?.data?.message || "Error obteniendo facturas de chat";
            success.value = null;
            throw err;
        } finally {
            loading.value = false;
        }
    };

    return {
        currentFactura,
        facturas,
        error,
        success,
        loading,
        createFactura,
        createFromSale,
        getFacturaById,
        getFacturasByBuyerId,
        getFacturasBySellerId,
        getFacturasByProductId,
        getFacturasByChatId
    };
}
