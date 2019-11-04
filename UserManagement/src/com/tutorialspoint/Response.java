package com.tutorialspoint;

import javax.xml.bind.annotation.XmlElement;

public class Response{
	
	private String message;
	
	Response(String message){
		this.message = message;
	}

	public Response() {
		
	}

	public String getMessage() {
		return message;
	}

	@XmlElement
	public void setMessage(String message) {
		this.message = message;
	}
}