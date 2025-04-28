import { EEstado } from './enums/EEstado';
import { EMoneda } from './enums/EMoneda';

export interface Producto {
    readonly id: string;
    idUsuario: string;
    idCategoria: string;
    imagenes: string[];
    marca: string;
    titulo: string;
    descripcion: string;
    estado: EEstado;
    precioCentimos: number;
    moneda: EMoneda;
    stock: number;
    fechaCreacion: string;
    fechaModificacion: string;
    fechaPublicacion: string;
    fechaBaja: string;
    direccion: string;
    activo: boolean;
    destacado: boolean;
}

export interface ProductoModify {
    readonly id?: string;
    idUsuario: string;
    idCategoria: string;
    imagenes: string[];
    marca: string;
    titulo: string;
    descripcion: string;
    estado: EEstado;
    precioCentimos: number;
    moneda: EMoneda;
    stock: number;
    fechaCreacion: string;
    fechaModificacion: string;
    fechaPublicacion: string;
    fechaBaja: string;
    direccion: string;
    activo: boolean;
    destacado: boolean;
}