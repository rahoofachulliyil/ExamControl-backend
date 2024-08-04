package com.mea.examcontrol.util.fcm;

import java.util.List;

public class PushNotificationRequest {
    private String title;
    private List<String> message;
    private String topic;
	
	//! We are not using token 
    //private String token;
    
    
  public PushNotificationRequest() {
		super();
	}
  
  
	public PushNotificationRequest(String title, List<String> message, String topic) {
		super();
		this.title = title;
		this.message = message;
		this.topic = topic;
		// this.token = token;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<String> getMessage() {
		return message;
	}
	public void setMessage(List<String> message) {
		this.message = message;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	// public String getToken() {
	// 	return token;
	// }
	// public void setToken(String token) {
	// 	this.token = token;
	// }  
    
    
}
