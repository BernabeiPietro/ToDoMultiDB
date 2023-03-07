package com.example.todoappmultidb.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.example.todoappmultidb.model.dto.ToDoDTO;

@Entity
public class ToDo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ElementCollection
	@CollectionTable(name = "todo_mapping", joinColumns = {
			@JoinColumn(name = "todo_map_id", referencedColumnName = "id") })
	@MapKeyColumn(name = "todo_action") /* where keys are stored */
	@Column(name = "doit") // where value are stored
	@Fetch(value = FetchMode.JOIN)
	private Map<String, Boolean> actionList;

	@Column(name = "local_date_time", columnDefinition = "TIMESTAMP")
	private LocalDateTime date;

	@ManyToOne
	@JoinColumn(name = "idUser", nullable = false)
	private User idOfUser;

	public ToDo() {
		super();
	}

	public ToDo(Long id, User idOfUser, Map<String, Boolean> toDo, LocalDateTime date) {
		super();
		this.id = id;
		this.actionList = toDo;
		this.date = date;
		this.idOfUser = idOfUser;
	}

	public ToDo(Long id, User idOfUser, LocalDateTime date) {
		super();
		this.id = id;
		this.actionList = new HashMap<>();
		this.date = date;
		this.idOfUser = idOfUser;
	}

	public ToDo(ToDoDTO td, User u) {
		this(td.getId(), u, td.getActions(), td.getDate());
	}

	public Map<String, Boolean> getToDo() {

		return actionList;

	}

	public void setToDo(Map<String, Boolean> toDo) {

		this.actionList = toDo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getLocalDateTime() {
		return date;
	}

	public void setLocalDateTime(LocalDateTime date) {
		this.date = date;
	}

	public void addToDoAction(String action, Boolean doIt) {

		this.actionList.put(action, doIt);

	}

	public User getIdOfUser() {
		return idOfUser;
	}

	public void setIdOfUser(User idOfUser) {
		this.idOfUser = idOfUser;
	}

	@Override
	public int hashCode() {
		return Objects.hash(actionList, date, id, idOfUser);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ToDo other = (ToDo) obj;
		return Objects.equals(actionList, other.actionList) && Objects.equals(date, other.date)
				&& Objects.equals(id, other.id) && Objects.equals(idOfUser, other.idOfUser);
	}

	@Override
	public String toString() {
		return "ToDo [id=" + id + ", actionList=" + actionList + ", date=" + date + ", idOfUser=" + idOfUser.getId()
				+ "]";
	}

}
