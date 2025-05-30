import axios from 'axios';

export const dashboardService = {
    /**
     * Obtiene las estadísticas generales del dashboard.
     * @param {number} year - Año a consultar.
     * @returns {Promise} Promesa con los datos del dashboard.
     */
    getStats: async (year: number) => {
        try {
            // Asegurarse de que el año se envía como un parámetro de consulta
            const response = await axios.get(`/dashboard/stats`, {
                params: {
                    year: year  // Esto asegura que se envíe correctamente como ?year=2023
                },
                withCredentials: true
            });
            return response.data;
        } catch (error) {
            console.error('Error al obtener estadísticas del dashboard:', error);
            throw error;
        }
    },

    /**
     * Obtiene las estadísticas de visitas para un producto específico.
     * @param {string} productoId - ID del producto.
     * @returns {Promise} Promesa con los datos de visitas del producto.
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
     * Obtiene las estadísticas de visitas para un mes específico.
     * @param {number} año - Año a consultar.
     * @param {number} mes - Mes a consultar (1-12).
     * @returns {Promise} Promesa con los datos de visitas del mes.
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
