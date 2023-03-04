package com.example.todoappmultidb.webcontroller;

import java.util.Collections;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.todoappmultidb.model.dto.ToDoDTO;
import com.example.todoappmultidb.repository.ToDoRepository;
import com.example.todoappmultidb.service.ToDoService;
import com.example.todoappmultidb.service.UserService;

import javassist.NotFoundException;

@Controller
@RequestMapping("/todo")
public class ToDoWebController {

	private static final String LIST_TODO_PAGE = "todo";
	private static final String EDIT_TODO_PAGE = "editToDo";
	private static final String MESSAGE = "message";
	@Autowired
	private ToDoService todoService;
	@Autowired
	private UserService userService;

	@GetMapping("/ofuser/{id}")
	public String getUserToDo(@RequestParam(required = false, defaultValue = "1") int db, @PathVariable long id,
			Model model) {
		userService.setDatabase(db);
		try {
			model.addAttribute("todo", userService.getToDoOfUser(id));
			model.addAttribute(MESSAGE, "");
		} catch (NotFoundException e) {
			model.addAttribute("todo", Collections.emptyMap());
			model.addAttribute(MESSAGE, e.getMessage());
		} 
		model.addAttribute("id", id);

		return LIST_TODO_PAGE;
	}

	@GetMapping("/edit/{id}")
	public String editToDo(@RequestParam(required = false, defaultValue = "1") int db, @PathVariable long id,
			Model model) {
		userService.setDatabase(db);
		try {

			model.addAttribute("todo", todoService.findByIdDTO(id));
			model.addAttribute(MESSAGE, "");
		} catch (NotFoundException e) {
			model.addAttribute("todo", Collections.emptyMap());
			model.addAttribute(MESSAGE, e.getMessage());
		}

		return EDIT_TODO_PAGE;
	}

	@GetMapping("/new/{id}")
	public String newToDo(@PathVariable long id, Model model) {
		model.addAttribute("todo", new ToDoDTO(-1L, id, new HashMap<>(), null));
		model.addAttribute(MESSAGE, "");
		return EDIT_TODO_PAGE;
	}

	@PostMapping("/addaction")
	public String addAction(ToDoDTO todo, String key, boolean value, Model model) throws NotFoundException {
		if (todo.getActions() == null)
			todo.setActions(new HashMap<>());
		if (!(key == null || key.isEmpty()))
			todo.addToDoAction(key, value);
		model.addAttribute("todo", todo);
		model.addAttribute(MESSAGE, "");
		return EDIT_TODO_PAGE;
	}

	@PostMapping("/save")
	public String saveToDo(@RequestParam(required = false, defaultValue = "0") int db, ToDoDTO todo, String key,
			Boolean value) {
		if (db == 0)
			userService.setDatabase(1);
		else
			userService.setDatabase(db);
		if (todo.getActions() == null || todo.getActions().isEmpty()) {
			todo.setActions(new HashMap<>());
		}
		if (!(key == null || key.isEmpty()))

			todo.addToDoAction(key, value);
		try {
			if (todo.getId() != -1)
				todoService.updateById(todo.getId(), todo);
			else {
				todo.setId(null);
				todoService.save(todo);
			}
		} catch (NotFoundException e) {
			return "redirect:/error/" + e.getMessage();
		}
		return db == 0 ? "redirect:/todo/ofuser/" + todo.getIdOfUser()
				: "redirect:/todo/ofuser/" + todo.getIdOfUser() + "?db=" + db;

	}
}
