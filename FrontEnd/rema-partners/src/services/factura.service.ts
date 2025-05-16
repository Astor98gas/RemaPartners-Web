import axios from "axios";
import type { FacturaEntity } from "@/models/factura";

const API_BASE_URL = "http://localhost:8080";
const FACTURA_API_URL = `${API_BASE_URL}/api/factura`;

export const facturaService = {
    // Create a new invoice
    async createFactura(factura: FacturaEntity) {
        return axios.post(`${FACTURA_API_URL}/create`, factura);
    },

    // Create an invoice from a product sale
    async createFromSale(idProducto: string, idComprador: string, idVendedor: string, cantidad: number, idChat: string) {
        return axios.post(`${FACTURA_API_URL}/createFromSale/${idProducto}/${idComprador}/${idVendedor}/${cantidad}/${idChat}`);
    },

    // Get invoice by ID
    async getFacturaById(id: string) {
        return axios.get(`${FACTURA_API_URL}/getById/${id}`);
    },

    // Get all invoices where the user is the buyer
    async getFacturasByBuyerId(idComprador: string) {
        return axios.get(`${FACTURA_API_URL}/getByBuyerId/${idComprador}`);
    },

    // Get all invoices where the user is the seller
    async getFacturasBySellerId(idVendedor: string) {
        return axios.get(`${FACTURA_API_URL}/getBySellerId/${idVendedor}`);
    },

    // Get all invoices for a specific product
    async getFacturasByProductId(idProducto: string) {
        return axios.get(`${FACTURA_API_URL}/getByProductId/${idProducto}`);
    },

    // Get all invoices associated with a chat
    async getFacturasByChatId(idChat: string) {
        return axios.get(`${FACTURA_API_URL}/getByChatId/${idChat}`);
    }
};
