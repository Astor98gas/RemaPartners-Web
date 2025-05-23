package com.arsansys.RemaPartners.models.firebase;

import java.util.Map;
import lombok.Data;

/**
 * Representa una nota que puede ser enviada a través de Firebase.
 * Contiene información como asunto, contenido, token de destinatario,
 * URL de imagen y datos adicionales.
 */
@Data
public class Note {
    /**
     * Asunto de la nota.
     */
    private String subject;
    /**
     * Contenido de la nota.
     */
    private String content;
    /**
     * Token del destinatario.
     */
    private String token;
    /**
     * URL de la imagen asociada a la nota.
     */
    private String imageUrl;
    /**
     * Datos adicionales en formato clave-valor.
     */
    private Map<String, String> data;
}
