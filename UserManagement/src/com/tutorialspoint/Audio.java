package com.tutorialspoint;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Audio {

	private int id;
	private String content;
	
	public Audio() {}
	
	public Audio(int id, String content) {
		this.id = id;
		this.content = content;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
}
