package org.hibernate.tutorial.domain;

import java.util.Date;

public class Event {

	public Event() {}
	
	private Long id;

    private String title;
    private Date date;
    
	public Long getId() {
		return id;
	}
	private void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
    
}
