import axios from 'axios';

// Función para generar datos de prueba aleatorios
const generateTestData = (year: number, isVentas = true) => {
    const data = {
        total: Math.floor(Math.random() * 50) + 10,
        importeTotal: Math.floor(Math.random() * 10000) + 1000,
        porMes: {} as Record<string, { cantidad: number, importe: number }>
    };

    // Generar datos aleatorios para cada mes
    for (let i = 1; i <= 12; i++) {
        data.porMes[i.toString()] = {
            cantidad: Math.floor(Math.random() * 10) + (i % 3 + 1), // Más ventas en algunos meses
            importe: Math.floor(Math.random() * 2000) + 200
        };
    }

    // Generar productos top si son ventas
    let productosTop = [];
    if (isVentas) {
        const productNames = [
            "Smartphone Galaxy S10", "iPad Pro 11", "Monitor LG 27\"", "MacBook Pro 16\"",
            "Teclado mecánico", "Ratón gaming", "Auriculares Bluetooth", "Cámara DSLR Canon"
        ];

        for (let i = 0; i < 5; i++) {
            productosTop.push({
                id: "prod-" + (i + 100),
                titulo: productNames[i],
                cantidadVentas: Math.floor(Math.random() * 10) + 5,
                importeTotal: Math.floor(Math.random() * 5000) + 500
            });
        }
    }

    return {
        [isVentas ? 'totalVentas' : 'totalCompras']: data.total,
        [isVentas ? 'importeTotalVentas' : 'importeTotalCompras']: data.importeTotal,
        [isVentas ? 'ventasPorMes' : 'comprasPorMes']: data.porMes,
        ...(isVentas ? { productosTopVentas: productosTop } : {})
    };
};

export const ventasDashboardService = {
    /**
     * Obtiene las estadísticas generales del dashboard de ventas/compras
     * @param {number} year - Año a consultar
     * @returns {Promise} Promesa con los datos del dashboard
     */
    getVentasStats: async (year: number) => {
        try {
            // Intentar obtener datos del backend
            try {
                const response = await axios.get(`/dashboard/ventas/stats`, {
                    params: {
                        year: year
                    },
                    withCredentials: true
                });
                return response.data;
            } catch (backendError) {
                console.warn('Backend endpoint no disponible, usando datos de prueba:', backendError);
                // Retornar datos de prueba si el endpoint no existe
                return generateTestData(year, true);
            }
        } catch (error) {
            console.error('Error al obtener estadísticas de ventas:', error);
            throw error;
        }
    },

    /**
     * Obtiene las estadísticas de ventas para un producto específico
     * @param {string} productoId - ID del producto
     * @returns {Promise} Promesa con los datos de ventas del producto
     */
    getProductoVentas: async (productoId: string) => {
        try {
            try {
                const response = await axios.get(`/dashboard/ventas/producto/${productoId}`, {
                    withCredentials: true
                });
                return response.data;
            } catch (backendError) {
                console.warn(`Backend endpoint no disponible para ventas del producto ${productoId}, usando datos de prueba:`, backendError);
                // Generar datos de prueba específicos para un producto
                const currentYear = new Date().getFullYear();
                const ventasPorMes = [] as Array<{ mes: number, año: number, cantidad: number, importe: number, ultimaActualizacion: string }>;

                // Generar datos para diferentes meses
                for (let mes = 1; mes <= 12; mes += 2) {  // Solo algunos meses para variedad
                    const cantidad = Math.floor(Math.random() * 5) + 1;
                    if (cantidad > 0) {  // Algunos meses sin ventas
                        ventasPorMes.push({
                            mes,
                            año: currentYear,
                            cantidad,
                            importe: cantidad * (Math.floor(Math.random() * 500) + 100),
                            ultimaActualizacion: new Date(currentYear, mes - 1,
                                Math.floor(Math.random() * 28) + 1).toISOString()
                        });
                    }
                }

                // Añadir algunos datos del año anterior
                for (let mes = 1; mes <= 12; mes += 3) {
                    const cantidad = Math.floor(Math.random() * 3) + 1;
                    if (cantidad > 0) {
                        ventasPorMes.push({
                            mes,
                            año: currentYear - 1,
                            cantidad,
                            importe: cantidad * (Math.floor(Math.random() * 450) + 120),
                            ultimaActualizacion: new Date(currentYear - 1, mes - 1,
                                Math.floor(Math.random() * 28) + 1).toISOString()
                        });
                    }
                }

                // Producto de prueba
                const testProducto = {
                    id: productoId,
                    titulo: "Producto de prueba " + productoId.substring(0, 5),
                    descripcion: "Descripción de producto de ejemplo para visualización",
                    marca: "Marca Ejemplo",
                    modelo: "Modelo X200",
                    estado: "USADO",
                    precioCentimos: 49900,
                    moneda: "EUR",
                    stock: 5,
                    activo: true,
                    destacado: false,
                    imagenes: [
                        "https://via.placeholder.com/400x300?text=Producto+Demo"
                    ]
                };

                return {
                    producto: testProducto,
                    totalVentas: ventasPorMes.reduce((total, item) => total + item.cantidad, 0),
                    importeTotal: ventasPorMes.reduce((total, item) => total + item.importe, 0),
                    ventasPorMes: ventasPorMes
                };
            }
        } catch (error) {
            console.error(`Error obteniendo ventas del producto ${productoId}:`, error);
            throw error;
        }
    },

    /**
     * Obtiene las estadísticas de ventas mensuales desglosadas
     * @param {number} año - Año a consultar
     * @param {number} mes - Mes a consultar (1-12)
     * @returns {Promise} Promesa con los datos de ventas del mes
     */
    getVentasPorMes: async (año: number, mes: number) => {
        try {
            const response = await axios.get(`/dashboard/ventas/mes/${año}/${mes}`, {
                withCredentials: true
            });
            return response.data;
        } catch (error) {
            console.error(`Error obteniendo ventas para ${mes}/${año}:`, error);
            throw error;
        }
    },

    /**
     * Obtiene las estadísticas de ventas para un usuario específico (como vendedor)
     * @param {string} userId - ID del usuario
     * @param {number} year - Año a consultar
     * @returns {Promise} Promesa con los datos de ventas del usuario
     */
    getVentasUsuario: async (userId: string, year: number) => {
        try {
            try {
                const response = await axios.get(`/dashboard/ventas/usuario/${userId}`, {
                    params: {
                        year: year
                    },
                    withCredentials: true
                });
                return response.data;
            } catch (backendError) {
                console.warn(`Backend endpoint no disponible para ventas del usuario ${userId}, usando datos de prueba:`, backendError);
                // Retornar datos de prueba si el endpoint no existe
                return generateTestData(year, true);
            }
        } catch (error) {
            console.error(`Error obteniendo ventas del usuario ${userId}:`, error);
            throw error;
        }
    },

    /**
     * Obtiene las estadísticas de compras para un usuario específico (como comprador)
     * @param {string} userId - ID del usuario
     * @param {number} year - Año a consultar
     * @returns {Promise} Promesa con los datos de compras del usuario
     */
    getComprasUsuario: async (userId: string, year: number) => {
        try {
            try {
                const response = await axios.get(`/dashboard/compras/usuario/${userId}`, {
                    params: {
                        year: year
                    },
                    withCredentials: true
                });
                return response.data;
            } catch (backendError) {
                console.warn(`Backend endpoint no disponible para compras del usuario ${userId}, usando datos de prueba:`, backendError);
                // Retornar datos de prueba si el endpoint no existe
                return generateTestData(year, false);
            }
        } catch (error) {
            console.error(`Error obteniendo compras del usuario ${userId}:`, error);
            throw error;
        }
    }
};

export default ventasDashboardService;
