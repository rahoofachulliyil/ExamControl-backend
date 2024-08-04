package com.mea.examcontrol.util.fcm;
import java.time.Duration;

import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Service
public class FCMService {
    private Logger logger = LoggerFactory.getLogger(FCMService.class);
    
    
    public ResponseEntity<?> sendMessageToTopic(PushNotificationRequest request)
            throws InterruptedException, ExecutionException {
        Message message = getPreconfiguredMessageWithoutData(request);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonOutput = gson.toJson(message);
        String response = sendAndGetResponse(message);
        logger.info("Sent message to token. Topic " + request.getTopic() + ", " + response+ " msg "+jsonOutput);
        return new ResponseEntity<>("Notification sent to : " + response, HttpStatus.OK);
    
    }
    
    private Message getPreconfiguredMessageWithoutData(PushNotificationRequest request) {
        return getPreconfiguredMessageBuilder(request).setTopic(request.getTopic())
                .build();
    }

   
    
    private Message.Builder getPreconfiguredMessageBuilder(PushNotificationRequest request) {
        AndroidConfig androidConfig = getAndroidConfig(request.getTopic());
        ApnsConfig apnsConfig = getApnsConfig(request.getTopic());
        return Message.builder()
                .setApnsConfig(apnsConfig).setAndroidConfig(androidConfig).setNotification(
                         Notification.builder().setTitle(request.getTitle())
                                .setBody(request.getMessage().toString()).build()
                       ) ;
    }
    
    private AndroidConfig getAndroidConfig(String topic) {
        return AndroidConfig.builder()
                .setTtl(Duration.ofMinutes(2).toMillis()).setCollapseKey(topic)
                .setPriority(AndroidConfig.Priority.HIGH)
                .setNotification(AndroidNotification.builder()
                      .setTag(topic).build()).build();
    }
    private ApnsConfig getApnsConfig(String topic) {
        return ApnsConfig.builder()
                .setAps(Aps.builder().setCategory(topic).setThreadId(topic).build()).build();
    }

    private String sendAndGetResponse(Message message) throws InterruptedException, ExecutionException {
        return FirebaseMessaging.getInstance().sendAsync(message).get();
    }

    //! These methods are not used in our case... these are for specific device notification by collecting their device tokens
    // private Message getPreconfiguredMessageToToken(PushNotificationRequest request) {
    //     return getPreconfiguredMessageBuilder(request).setToken(request.getToken())
    //             .build();
    // }
    
    // private Message getPreconfiguredMessageWithData(Map<String, String> data, PushNotificationRequest request) {
    //     return getPreconfiguredMessageBuilder(request).putAllData(data).setToken(request.getToken())
    //             .build();
    // }
   
}
