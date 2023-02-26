package com.example.todoappmultidb.model.dto;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

import org.springframework.lang.NonNull;

public class ToDoDTO {

	private Long id;

	private Map<String, Boolean> actions;
	private LocalDateTime date;
	
	private Long idOfUser;

	public ToDoDTO() {

	}

	public ToDoDTO(Long id, Long idOfUser, Map<String, Boolean> actions, LocalDateTime date) {
		this.id = id;
		this.idOfUser = idOfUser;
		this.actions = actions;
		this.date = date;
	}

	public Map<String, Boolean> getActions() {
		return actions;
	}

	public void setActions(Map<String, Boolean> actions) {
		this.actions = actions;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	@Override
	public String toString() {
		return "ToDoDTO [id=" + id + ", toDo=" + actions + ", date=" + date + ", idOfUser=" + idOfUser + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(date, id, idOfUser, actions);
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
				&& Objects.equals(idOfUser, other.idOfUser) && Objects.equals(actions, other.actions);
	}

	public void addToDoAction(String action, Boolean doIt) {
		this.actions.put(action, doIt);
	}

}