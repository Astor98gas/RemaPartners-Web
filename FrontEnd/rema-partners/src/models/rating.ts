// Representa una valoración de un usuario a un vendedor
export interface Rating {
    id: string;
    sellerId: string;      // ID del vendedor valorado
    userId: string;        // ID del usuario que deja la valoración
    username: string;      // Nombre del usuario que deja la valoración
    rating: number;        // Puntuación de 1 a 5 estrellas
    comment: string;       // Comentario de la valoración
    reply?: string;        // Respuesta del vendedor a la valoración (opcional)
    createdAt: string;     // Fecha de creación de la valoración
    updatedAt: string;     // Fecha de última actualización
}

// DTO para crear o actualizar una valoración
export interface RatingFormData {
    sellerId: string;
    rating: number;
    comment: string;
}

// DTO para añadir una respuesta a una valoración
export interface RatingReplyData {
    ratingId: string;
    reply: string;
}
