package com.mea.examcontrol.util.fcm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PushNotificationService {
	
    private Logger logger = LoggerFactory.getLogger(PushNotificationService.class);
    
    private FCMService fcmService;
    
    public PushNotificationService(FCMService fcmService) {
        this.fcmService = fcmService;
    }
    
    
    public ResponseEntity<?> sendPushNotificationToTopic(PushNotificationRequest request) {
        try {
           return fcmService.sendMessageToTopic(request);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
   
}