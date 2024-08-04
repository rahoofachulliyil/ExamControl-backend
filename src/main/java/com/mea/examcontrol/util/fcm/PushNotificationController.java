package com.mea.examcontrol.util.fcm;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PushNotificationController {
	
	
    private PushNotificationService pushNotificationService;
    
    public PushNotificationController(PushNotificationService pushNotificationService) {
        this.pushNotificationService = pushNotificationService;
    }
    
    @PostMapping("/notification/topic")
    public ResponseEntity<?> sendTopicNotification(@RequestBody PushNotificationRequest request) {
        return  pushNotificationService.sendPushNotificationToTopic(request);
    }
    
}
