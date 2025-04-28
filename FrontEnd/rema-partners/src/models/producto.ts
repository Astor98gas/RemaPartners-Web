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
    estado: EEstado; // Cambiado a enum
    precioCentimos: number;
    moneda: EMoneda; // Cambiado a enum
    stock: number;
    fechaCreacion: string;
    fechaModificacion: string;
    fechaPublicacion: string;
}

export interface ProductoModify {
    readonly id?: string;
    idUsuario: string;
    idCategoria: string;
    imagenes: string[];
    marca: string;
    titulo: string;
    descripcion: string;
    estado: EEstado; // Cambiado a enum
    precioCentimos: number;
    moneda: EMoneda; // Cambiado a enum
    stock: number;
    fechaCreacion: string;
    fechaModificacion: string;
    fechaPublicacion: string;
}