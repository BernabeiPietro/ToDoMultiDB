package com.example.todoappmultidb.webcontroller;

import java.util.Collections;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.todoappmultidb.dto.ToDoDTO;
import com.example.todoappmultidb.dto.UserDTO;
import com.example.todoappmultidb.service.ToDoService;

import javassist.NotFoundException;

@Controller
public class ToDoWebController {

	private static final String LIST_TODO_PAGE = "todo";
	private static final String EDIT_TODO_PAGE = "editToDo";
	private static final String MESSAGE = "message";
	@Autowired
	private ToDoService todoService;

	@GetMapping("/todo/ofuser/{id}")
	public String getUserToDo(@PathVariable long id, Model model) {
		try {
			model.addAttribute("todo", todoService.findByUserId(new UserDTO(id, null, null)));
			model.addAttribute(MESSAGE, "");
		} catch (NotFoundException e) {
			model.addAttribute("todo", Collections.emptyMap());
			model.addAttribute(MESSAGE, e.getMessage());
		}
		model.addAttribute("id", id);

		return LIST_TODO_PAGE;
	}

	@GetMapping("/todo/edit/{id}")
	public String editToDo(@PathVariable long id, Model model) {
		try {
			model.addAttribute("todo", todoService.getToDoById(id));
			model.addAttribute(MESSAGE, "");
		} catch (NotFoundException e) {
			model.addAttribute(MESSAGE, e.getMessage());
		}

		return EDIT_TODO_PAGE;
	}

	@GetMapping("/todo/new/{id}")
	public String newToDo(@PathVariable long id, Model model) {
		model.addAttribute("todo", new ToDoDTO(-1L, id, new HashMap<>(), null));
		model.addAttribute(MESSAGE, "");
		return EDIT_TODO_PAGE;
	}

	@PostMapping("/todo/addaction")
	public String addAction(ToDoDTO todo, String key, boolean value, Model model) {
		if (todo.getActions() == null)
			todo.setActions(new HashMap<>());
		if (!(key == null || key.isEmpty()))
			todo.addToDoAction(key, value); 
		model.addAttribute("todo", todo);
		model.addAttribute(MESSAGE, "");
		return EDIT_TODO_PAGE;
	}

	@PostMapping("/todo/save")
	public String saveToDo(ToDoDTO todo, String key, Boolean value) {
		if (todo.getActions() == null || todo.getActions().isEmpty()) {
			todo.setActions(new HashMap<String, Boolean>());
		}
		if (!(key == null || key.isEmpty()))

			todo.addToDoAction(key, value);
		try {
			if (todo.getId() != -1)
				todoService.updateToDo(todo.getId(), todo);
			else {
				todo.setId(null);
				todoService.saveToDo(todo);
			}
		} catch (NotFoundException e) {
			return "redirect:/error/"+e.getMessage();
		}
		return "redirect:/todo/ofuser/" + todo.getIdOfUser();

	} 
}
