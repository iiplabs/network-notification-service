package com.iiplabs.nns.core.model.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@SuppressWarnings("serial")
public class ErrorDetails implements Serializable {

	private final String details;
	private final String message;
	private int status;
	@JsonProperty("timestamp")
	private final Date timeStamp;

	private ErrorDetails(final Date timeStamp, final String message, final String details, final int status) {
		this.details = details;
		this.message = message;
		this.timeStamp = timeStamp;
		this.status = status;
	}

	public static class ErrorDetailsBuilder {
		private String nestedMessage;
		private String nestedDetails;
		private Date nestedTimeStamp;
		private int nestedStatus;

		public ErrorDetailsBuilder(final String newMessage) {
			this.nestedMessage = newMessage;
		}

		public ErrorDetailsBuilder timeStamp(final Date newTimeStamp) {
			this.nestedTimeStamp = newTimeStamp;
			return this;
		}

		public ErrorDetailsBuilder details(final String newDetails) {
			this.nestedDetails = newDetails;
			return this;
		}

		public ErrorDetailsBuilder status(final int newStatus) {
			this.nestedStatus = newStatus;
			return this;
		}

		public ErrorDetails build() {
			return new ErrorDetails(nestedTimeStamp, nestedMessage, nestedDetails, nestedStatus);
		}
	}

}
