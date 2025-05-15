import axios from 'axios';

export const dashboardService = {
    /**
     * Obtiene las estadísticas generales del dashboard
     * @returns {Promise} Promesa con los datos del dashboard
     */
    getStats: async () => {
        try {
            const response = await axios.get('/dashboard/stats', {
                withCredentials: true
            });
            return response.data;
        } catch (error) {
            console.error('Error obteniendo estadísticas del dashboard:', error);
            throw error;
        }
    },

    /**
     * Obtiene las estadísticas de visitas para un producto específico
     * @param {string} productoId - ID del producto
     * @returns {Promise} Promesa con los datos de visitas del producto
     */
    getProductoVisitas: async (productoId: string) => {
        try {
            const response = await axios.get(`/dashboard/producto/${productoId}/visitas`, {
                withCredentials: true
            });
            return response.data;
        } catch (error) {
            console.error(`Error obteniendo visitas del producto ${productoId}:`, error);
            throw error;
        }
    },

    /**
     * Obtiene las estadísticas de visitas para un mes específico
     * @param {number} año - Año a consultar
     * @param {number} mes - Mes a consultar (1-12)
     * @returns {Promise} Promesa con los datos de visitas del mes
     */
    getVisitasPorMes: async (año: number, mes: number) => {
        try {
            const response = await axios.get(`/dashboard/visitas/mes/${año}/${mes}`, {
                withCredentials: true
            });
            return response.data;
        } catch (error) {
            console.error(`Error obteniendo visitas para ${mes}/${año}:`, error);
            throw error;
        }
    }
};

export default dashboardService;
