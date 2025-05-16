// Factura model to mirror the backend FacturaEntity

export interface FacturaEntity {
    id?: string;
    idProducto: string;
    idComprador: string;
    idVendedor: string;
    cantidad: number;
    precioUnitarioCentimos: number;
    iva: number;
    moneda: string;
    estadoFactura: EstadoFactura;
    fechaCreacion?: string;
    fechaPago?: string;
    notasAdicionales?: string;
    idChat?: string;
    nombreProducto?: string;
}

export enum EstadoFactura {
    PENDIENTE = "PENDIENTE",
    PAGADA = "PAGADA",
    CANCELADA = "CANCELADA"
}

export interface FacturaResumen {
    id: string;
    nombreProducto: string;
    cantidad: number;
    total: number;
    moneda: string;
    fechaCreacion: string;
    estado: EstadoFactura;
}
