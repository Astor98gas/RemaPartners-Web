export interface MensajeEntity {
    id?: string;
    idEmisor: string;
    fecha?: string;
    mensaje: string;
    leido?: boolean;
}

export interface ChatEntity {
    id?: string;
    idProducto: string;
    idComprador: string;
    idVendedor: string;
    mensajes: MensajeEntity[];
    fechaCreacion: string;
    ultimaActualizacion?: string;
    activo: boolean;
}