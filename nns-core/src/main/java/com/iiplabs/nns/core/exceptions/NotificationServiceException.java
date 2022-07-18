package com.iiplabs.nns.core.exceptions;

public class NotificationServiceException extends RuntimeException {

	private int statusCode;
	
	public NotificationServiceException(String message) {
		super(message);
	}

	public NotificationServiceException(String message, int statusCode) {
		this(message);
		this.statusCode = statusCode;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

}
