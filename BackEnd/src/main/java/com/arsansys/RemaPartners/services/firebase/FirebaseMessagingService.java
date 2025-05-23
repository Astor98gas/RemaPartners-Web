package com.arsansys.RemaPartners.services.firebase;

import org.springframework.stereotype.Service;

import com.arsansys.RemaPartners.models.firebase.Note;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;

/**
 * Servicio para el envío de notificaciones push utilizando Firebase Cloud Messaging.
 */
@Service
@RequiredArgsConstructor
public class FirebaseMessagingService {

    /**
     * Envía una notificación push a un dispositivo específico utilizando un token de Firebase.
     *
     * @param note  Objeto Note que contiene el título, contenido, imagen y datos adicionales de la notificación.
     * @param token Token del dispositivo al que se enviará la notificación.
     * @return String con el ID del mensaje enviado por Firebase.
     * @throws FirebaseMessagingException Si ocurre un error al enviar la notificación.
     */
    public String sendNotification(Note note, String token) throws FirebaseMessagingException {
        Notification notification = Notification
                .builder()
                .setTitle(note.getSubject())
                .setBody(note.getContent())
                .setImage(note.getImageUrl())
                .build();

        Message message = Message
                .builder()
                .setToken(token)
                .setNotification(notification)
                .putAllData(note.getData())
                .build();

        return FirebaseMessaging.getInstance().send(message);
    }
}
