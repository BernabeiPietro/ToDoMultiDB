package com.example.todoappmultidb.model.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.OneToMany;

import com.example.todoappmultidb.model.ToDo;

public class UserDTO {
	private Long id;
	private List<ToDoDTO> todo;
	private String name;
	private String email;


	public UserDTO(Long id, String name,List<ToDoDTO> todo, String email) {
		// TODO Auto-generated constructor stub

		this.id = id;
		this.name = name;
		this.email = email;
		this.todo=todo;
	}

	public UserDTO() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<ToDoDTO> getTodo() {
		return todo;
	}

	public void setTodo(List<ToDoDTO> todo) {
		this.todo = todo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, id, name, todo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserDTO other = (UserDTO) obj;
		return Objects.equals(email, other.email) && Objects.equals(id, other.id) && Objects.equals(name, other.name)
				&& Objects.equals(todo, other.todo);
	}

	@Override
	public String toString() {
		return "UserDTO [id=" + id + ", todo=" + todo + ", name=" + name + ", email=" + email + "]";
	}

}
