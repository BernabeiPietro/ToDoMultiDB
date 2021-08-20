package com.example.todoappmultidb.rest;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.todoappmultidb.model.ToDo;
import com.example.todoappmultidb.model.User;
import com.example.todoappmultidb.model.dto.ToDoDTO;
import com.example.todoappmultidb.service.ToDoService;


@RestController
@RequestMapping("/api/todo")
public class ToDoRestController {

	@Autowired
	private ToDoService todoService;

	@GetMapping
	public List<ToDoDTO> getToDo() {
		return todoService.getAllToDo();
		
	}
	
}
