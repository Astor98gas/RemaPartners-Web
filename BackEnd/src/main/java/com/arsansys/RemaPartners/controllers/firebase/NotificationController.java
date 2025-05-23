package com.arsansys.RemaPartners.controllers.firebase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.arsansys.RemaPartners.models.firebase.Note;
import com.arsansys.RemaPartners.services.firebase.FirebaseMessagingService;
import com.google.firebase.messaging.FirebaseMessagingException;

/**
 * Controlador REST para el envío de notificaciones push mediante Firebase.
 */
@RestController
public class NotificationController {

    @Autowired
    private FirebaseMessagingService firebaseService;

    /**
     * Envía una notificación push a través de Firebase.
     *
     * @param note Objeto con los datos de la notificación.
     * @return Resultado del envío de la notificación.
     * @throws FirebaseMessagingException Si ocurre un error al enviar la
     *                                    notificación.
     */
    @PostMapping("/sendNotification")
    public String sendNotification(@RequestBody Note note) throws FirebaseMessagingException {
        return firebaseService.sendNotification(note, note.getToken());
    }
}
