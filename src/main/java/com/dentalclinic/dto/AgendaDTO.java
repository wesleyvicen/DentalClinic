package com.dentalclinic.dto;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;

import com.dentalclinic.model.Agenda;

public class AgendaDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	private String title;
	private String start;
	private String end;
	private String allDay;
	private Boolean status;

	public AgendaDTO() {
	}

	public AgendaDTO(Integer id, String title, String start, String end, String allDay, Boolean status) {
		super();
		this.id = id;
		this.title = title;
		this.start = start;
		this.end = end;
		this.allDay = allDay;
		this.status = status;
	}

	public AgendaDTO(String title, String start, String end, String allDay, Boolean status) {
		super();
		this.title = title;
		this.start = start;
		this.end = end;
		this.allDay = allDay;
		this.status = status;
	}

	public AgendaDTO(Agenda entity) {
		this.id = entity.getId();
		this.title = entity.getTitle();
		DateTimeFormatter dateFormater = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		DateTimeFormatter dateFormaterH = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		this.start = entity.getStart().format(dateFormaterH);
		this.end = entity.getEnd().format(dateFormaterH);
		this.allDay = entity.getAllDay().format(dateFormater);
		this.status = entity.getStatus();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String getAllDay() {
		return allDay;
	}

	public void setAllDay(String allDay) {
		this.allDay = allDay;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

}
