package model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Message {
	private String status;
	private List<String> messages = new LinkedList<>();
	private String stackTrace = "";
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<String> getMessages() {
		return messages;
	}
	public void addMessage(String message) {
		messages.add(message);
	}
	public String getStackTrace() {
		return stackTrace;
	}
	public void setStackTrace(String stackTrace) {
		this.stackTrace = stackTrace;
	}
	
	public Map<String, Object> asMap() {
		Map<String, Object> result = new HashMap<>();
		result.put("message", getMessages());
		result.put("status", getStatus());
		result.put("stackTrace", getStackTrace());
		return result;
	}
	
}
