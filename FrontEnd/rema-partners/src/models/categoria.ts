export interface Categoria {
    readonly id: string
    titulo: string
    descripcion: string
    campos: Array<string>
}

export interface CategoriaModify {
    readonly id?: string
    titulo: string
    descripcion: string
    campos: Array<string>
}