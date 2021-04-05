package com.dentalclinic.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.dentalclinic.model.Agenda;
import com.fasterxml.jackson.annotation.JsonFormat;

public class AgendaDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String title;
	@DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime start;
	@DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime end;
	@DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate allDay;
	private Boolean status;

	public AgendaDTO() {

	}

	public AgendaDTO(Agenda obj) {
		this.id = obj.getId();
		this.title = obj.getTitle();
		this.start = obj.getStart();
		this.end = obj.getEnd();
		this.allDay = obj.getAllDay();
		this.status = obj.getStatus();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public LocalDateTime getStart() {
		return start;
	}

	public void setStart(LocalDateTime start) {
		this.start = start;
	}

	public LocalDateTime getEnd() {
		return end;
	}

	public void setEnd(LocalDateTime end) {
		this.end = end;
	}

	public LocalDate getAllDay() {
		return allDay;
	}

	public void setAllDay(LocalDate allDay) {
		this.allDay = allDay;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

}
