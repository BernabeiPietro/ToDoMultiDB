package com.example.todoappmultidb.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.example.todoappmultidb.model.dto.UserDTO;

@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@OneToMany(mappedBy = "idOfUser", orphanRemoval = true)
	@Fetch(value = FetchMode.JOIN)
	private List<ToDo> todo;
	private String name;
	private String email;

	public User() {
		super();
	}

	public User(Long id, List<ToDo> todo, String name, String email) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.todo = todo;
	}

	public User(Long id, String name, String email) {
		this(id, new ArrayList<>(), name, email);
	}

	public User(UserDTO u) {
		this(u.getId(), u.getName(), u.getEmail());
	}

	public User(UserDTO u, List<ToDo> listToDo) {
		this(u.getId(), listToDo, u.getName(), u.getEmail());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public List<ToDo> getToDo() {
		return todo;
	}

	public void addToDo(ToDo td) {
		todo.add(td);
	}

	public void setTodo(List<ToDo> todo) {
		this.todo = todo;
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
		if ((obj == null) || (getClass() != obj.getClass()))
			return false;
		User other = (User) obj;

		return Objects.equals(email, other.email) && Objects.equals(id, other.id) && Objects.equals(name, other.name);

	}

	@Override
	public String toString() {
		return "User [id=" + id + ", todo=" + todo + ", name=" + name + ", email=" + email + "]";
	}
}