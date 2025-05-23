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
            success.value = "Invoice created successfully";
            return response.data;
        } catch (err: any) {
            console.error("Error creating invoice:", err);
            error.value = err.response?.data?.message || "Error creating invoice";
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
            success.value = "Invoice created successfully from sale";
            return response.data;
        } catch (err: any) {
            console.error("Error creating invoice from sale:", err);
            error.value = err.response?.data?.message || "Error creating invoice from sale";
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
            console.error("Error fetching invoice by ID:", err);
            error.value = err.response?.data?.message || "Error fetching invoice by ID";
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
            console.log(`useFactura: Fetching invoices for buyer with ID: ${idComprador}`);
            const response = await facturaService.getFacturasByBuyerId(idComprador);
            console.log("useFactura: Buyer invoices API response:", response);
            facturas.value = response.data;
            return response.data;
        } catch (err: any) {
            console.error("Error fetching buyer invoices:", err);
            error.value = err.response?.data?.message || "Error fetching buyer invoices";
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
            console.log(`useFactura: Fetching invoices for seller with ID: ${idVendedor}`);
            const response = await facturaService.getFacturasBySellerId(idVendedor);
            console.log("useFactura: Seller invoices API response:", response);
            facturas.value = response.data;
            return response.data;
        } catch (err: any) {
            console.error("Error fetching seller invoices:", err);
            error.value = err.response?.data?.message || "Error fetching seller invoices";
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
            console.error("Error fetching product invoices:", err);
            error.value = err.response?.data?.message || "Error fetching product invoices";
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
            console.error("Error fetching chat invoices:", err);
            error.value = err.response?.data?.message || "Error fetching chat invoices";
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
