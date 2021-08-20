package com.example.todoappmultidb.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

}
