package com.example.todoappmultidb.webcontroller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.todoappmultidb.dto.ToDoDTO;
import com.example.todoappmultidb.dto.UserDTO;
import com.example.todoappmultidb.service.ToDoService;
import com.fasterxml.jackson.databind.ObjectMapper;

import javassist.NotFoundException;

@Controller
public class ToDoWebController {

	private final String MESSAGE = "message";
	@Autowired
	private ToDoService todoService;

	@GetMapping("/todo/ofuser/{id}")
	public String getUserToDo(@PathVariable long id, Model model) {
		try {
			model.addAttribute("todo", todoService.findByUserId(new UserDTO(id, null, null)));
			model.addAttribute(MESSAGE, "");
		} catch (NotFoundException e) {
			model.addAttribute(MESSAGE, e.getMessage());
		}
		return "todo";
	}

	@GetMapping("/todo/edit/{id}")
	public String editToDo(@PathVariable long id, Model model) {
		try {
			model.addAttribute("todo", todoService.getToDoById(id));
			model.addAttribute(MESSAGE, "");
		} catch (NotFoundException e) {
			model.addAttribute(MESSAGE, e.getMessage());
		}
		return "editToDo";
	}

	@GetMapping("/todo/new")
	public String newToDo(Model model) {
		model.addAttribute("todo", new ToDoDTO());
		model.addAttribute(MESSAGE, "");
		return "editToDo";
	}

	@PostMapping("/todo/save")
	public String saveToDo(ToDoDTO todo) {
		if(todo.getActions()==null) {
			todo.setActions(new HashMap<String, Boolean>());
		
		}
		try {

			if(todo.getId()!=-1)
				todoService.updateToDo(todo.getId(), todo);
			else {
				todo.setId(null);
				todoService.saveToDo(todo);
			}
		} catch (NotFoundException e) {
			e.printStackTrace();
		}
		return "redirect:/";

	}
}
