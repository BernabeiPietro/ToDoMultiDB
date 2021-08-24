package com.example.todoappmultidb.rest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.todoappmultidb.model.dto.ToDoDTO;
import com.example.todoappmultidb.service.ToDoService;

@RestController
@RequestMapping("/api/todo")
public class ToDoRestController {

	@Autowired
	private ToDoService todoService;

	@GetMapping
	public List<ToDoDTO> getToDo() {
		try {
			return todoService.getAllToDo();

		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NO_CONTENT, e.getMessage());

		}

	}

	@GetMapping("/{id}")
	public ToDoDTO getToDoById(@PathVariable long id) {
		try {
			return todoService.getToDoById(id);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NO_CONTENT, e.getMessage());
		}

	}

	@PostMapping("/new")
	@ResponseStatus(HttpStatus.CREATED)
	public ToDoDTO postNewToDoDTO(@RequestBody ToDoDTO todo) {
		if (todo.getIdOfUser()!=null) {
			todo.setId(1l);
			return todo;
		} else {
			throw new ResponseStatusException(HttpStatus.CONFLICT);
		}
	}

}
