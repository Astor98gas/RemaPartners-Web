import { ref } from "vue";
import { facturaService } from "@/services/factura.service";
import type { FacturaEntity } from "@/models/factura";

export function useFactura() {
    const currentFactura = ref<FacturaEntity | null>(null);
    const facturas = ref<FacturaEntity[]>([]);
    const error = ref<string | null>(null);
    const success = ref<string | null>(null);
    const loading = ref<boolean>(false);

    /**
     * Creates a new invoice
     * 
     * @param factura - The invoice data to create
     * @returns The created invoice object
     * @throws Error if the invoice cannot be created
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
     * Creates an invoice from a product sale
     * 
     * @param idProducto - The ID of the sold product
     * @param idComprador - The ID of the buyer
     * @param idVendedor - The ID of the seller
     * @param cantidad - The quantity of items sold
     * @param idChat - The ID of the chat where the sale took place
     * @returns The created invoice object
     * @throws Error if the invoice cannot be created
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
     * Gets an invoice by its ID
     * 
     * @param id - The ID of the invoice to retrieve
     * @returns The invoice object
     * @throws Error if the invoice cannot be fetched
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
     * Gets all invoices where the user is the buyer
     * 
     * @param idComprador - The ID of the buyer
     * @returns Array of invoices
     * @throws Error if the invoices cannot be fetched
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
     * Gets all invoices where the user is the seller
     * 
     * @param idVendedor - The ID of the seller
     * @returns Array of invoices
     * @throws Error if the invoices cannot be fetched
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
     * Gets all invoices for a specific product
     * 
     * @param idProducto - The ID of the product
     * @returns Array of invoices
     * @throws Error if the invoices cannot be fetched
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
     * Gets all invoices associated with a chat
     * 
     * @param idChat - The ID of the chat
     * @returns Array of invoices
     * @throws Error if the invoices cannot be fetched
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
