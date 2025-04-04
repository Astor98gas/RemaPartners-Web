package com.arsansys.RemaPartners.services.firebase;

import org.springframework.stereotype.Service;

import com.arsansys.RemaPartners.models.firebase.Note;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FirebaseMessagingService {

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
