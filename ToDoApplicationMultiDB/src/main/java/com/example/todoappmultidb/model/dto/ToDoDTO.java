package com.example.todoappmultidb.model.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;

import com.example.todoappmultidb.model.ToDo;
import com.example.todoappmultidb.model.User;
import com.fasterxml.jackson.annotation.JsonFormat;

public class ToDoDTO {

	private Long id;

	private Map<String, Boolean> toDo;
	private LocalDateTime date;
	private Long idOfUser;

	public ToDoDTO(){
		
	}
	
	public ToDoDTO(Long id, Long idOfUser, Map<String, Boolean> todo, LocalDateTime date) {
		this.id=id;
		this.idOfUser=idOfUser;
		this.toDo=todo;
		this.date=date;
	}
	public ToDoDTO(ToDo td)
	{
		this(td.getId(),td.getIdOfUser().getId(),td.getToDo(),td.getLocalDateTime());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Map<String, Boolean> getToDo() {
		return toDo;
	}

	public void setToDo(Map<String, Boolean> toDo) {
		this.toDo = toDo;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public Long getIdOfUser() {
		return idOfUser;
	}

	public void setIdOfUser(Long idOfUser) {
		this.idOfUser = idOfUser;
	}
	public void addToDoAction(String action, Boolean doIt) {

		this.toDo.put(action, doIt);

	}
	@Override
	public int hashCode() {
		return Objects.hash(date, id, idOfUser, toDo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ToDoDTO other = (ToDoDTO) obj;
		return Objects.equals(date, other.date) && Objects.equals(id, other.id)
				&& Objects.equals(idOfUser, other.idOfUser) && Objects.equals(toDo, other.toDo);
	}

	@Override
	public String toString() {
		return "ToDoDTO [id=" + id + ", toDo=" + toDo + ", date=" + date + ", idOfUser=" + idOfUser + "]";
	}

	

	
}
