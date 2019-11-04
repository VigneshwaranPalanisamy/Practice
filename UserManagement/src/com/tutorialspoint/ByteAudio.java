package com.tutorialspoint;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ByteAudio {
	
	private int id;
	private byte[] content;
	
	public ByteAudio() {}
	
	public ByteAudio(int id, byte[] content) {
		this.id = id;
		this.content = content;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public byte[] getContent() {
		return content;
	}
	
	public void setContent(byte[] content) {
		this.content = content;
	}

}
