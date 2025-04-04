package com.arsansys.RemaPartners.controllers.firebase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.arsansys.RemaPartners.models.firebase.Note;
import com.arsansys.RemaPartners.services.firebase.FirebaseMessagingService;
import com.google.firebase.messaging.FirebaseMessagingException;

@RestController
public class NotificationController {

    @Autowired
    private FirebaseMessagingService firebaseService;

    @PostMapping("/sendNotification")
    public String sendNotification(@RequestBody Note note) throws FirebaseMessagingException {
        return firebaseService.sendNotification(note, note.getToken());
    }
}
