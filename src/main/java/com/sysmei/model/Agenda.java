package com.sysmei.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Agenda implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
	@Column(name = "start")
	@DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime start;
	@Column(name = "end")
	@DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime end;
	@Column(name = "allDay")
	@DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate allDay;
	@OneToOne
	@JoinColumn(name = "paciente_id", referencedColumnName = "id")
	private Paciente paciente;
	@JsonIgnore
	@ManyToOne()
	@JoinColumn(name = "login_usuario", referencedColumnName = "login")
	private Usuario usuario;
	private Double valor;
	private Integer status;
	private String pagamento;
	private String detalhes;
	@ManyToOne()
	@JoinColumn(name = "prestador_id", referencedColumnName = "id")
	private Prestador prestador;

	public Agenda() {
	}

	public Agenda(Long id, String title, LocalDateTime start, LocalDateTime end, LocalDate allDay, Paciente paciente,
				  Double valor, Integer status, String pagamento, String detalhes) {
		super();
		this.id = id;
		this.title = title;
		this.start = start;
		this.end = end;
		this.allDay = allDay;
		this.paciente = paciente;
		this.setValor(valor);
		this.status = status;
		this.setPagamento(pagamento);
		this.setDetalhes(detalhes);
	}

	public Agenda(String title, LocalDateTime start, LocalDateTime end, LocalDate allDay, Paciente paciente,
			Double valor, Integer status, String pagamento, String detalhes) {
		super();
		this.title = title;
		this.start = start;
		this.end = end;
		this.allDay = allDay;
		this.paciente = paciente;
		this.setValor(valor);
		this.status = status;
		this.setPagamento(pagamento);
		this.setDetalhes(detalhes);
	}

	public Agenda(Long id, String title, LocalDateTime start, LocalDateTime end, LocalDate allDay, Double valor,
			int status, String pagamento, String detalhes) {
		super();
		this.title = title;
		this.start = start;
		this.end = end;
		this.allDay = allDay;
		this.setValor(valor);
		this.status = status;
		this.setPagamento(pagamento);
		this.setDetalhes(detalhes);
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
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

	public Paciente getPaciente() {
		return paciente;
	}

	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((paciente == null) ? 0 : paciente.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Agenda other = (Agenda) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (paciente == null) {
			if (other.paciente != null)
				return false;
		} else if (!paciente.equals(other.paciente))
			return false;
		return true;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public String getPagamento() {
		return pagamento;
	}

	public void setPagamento(String pagamento) {
		this.pagamento = pagamento;
	}

	public String getDetalhes() {
		return detalhes;
	}

	public void setDetalhes(String detalhes) {
		this.detalhes = detalhes;
	}

	public Prestador getPrestador() {
		return prestador;
	}

	public void setPrestador(Prestador prestador) {
		this.prestador = prestador;
	}
	
	

}
