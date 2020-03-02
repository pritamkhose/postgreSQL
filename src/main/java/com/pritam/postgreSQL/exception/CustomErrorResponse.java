package com.pritam.postgreSQL.exception;

import java.util.Date;

// https://mkyong.com/spring-boot/spring-rest-error-handling-example/

public class CustomErrorResponse {

	private Date timestamp = new Date();
	private int status;
	private String error;
	
	public Date getTimestamp() {
		return timestamp;
	}
	public void Date(Date timestamp) {
		this.timestamp = timestamp;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}

}
