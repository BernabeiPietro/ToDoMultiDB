package com.example.todoappmultidb.webcontroller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.todoappmultidb.dto.UserDTO;
import com.example.todoappmultidb.model.User;
import com.example.todoappmultidb.service.UserService;

@Controller
public class HomeWebController {
	private final String MESSAGE = "message";
	@Autowired
	private UserService userService;

	@GetMapping("/")
	public String index(Model model) {
		List<UserDTO> allUser = userService.getAllUser();
		model.addAttribute("users", allUser);
		model.addAttribute(MESSAGE, allUser.isEmpty() ? "No user" : "");
		return "index";
	}

	@GetMapping("/edit/{id}")
	public String editUser(@PathVariable long id, Model model) {
		UserDTO user = userService.getUserById(id);
		model.addAttribute("user", user);
		model.addAttribute(MESSAGE, user == null ? "No user found with id: " + id : "");
		return "edit";
	}

	@GetMapping("/new")
	public String newUser(Model model) {
		model.addAttribute("user", new UserDTO());
		model.addAttribute(MESSAGE, "");
		return "edit";
	}

	@PostMapping("/save")
	public String saveUser(UserDTO user) {
		final Long id = user.getId();
		if (id == null) {
			user.setTodo(new ArrayList<>());
			userService.insertNewUser(user);
		} else {
			userService.updateUserById(id, user);
		}
		return "redirect:/";
	}
}
