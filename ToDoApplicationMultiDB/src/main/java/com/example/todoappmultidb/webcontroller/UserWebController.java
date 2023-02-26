package com.example.todoappmultidb.webcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.todoappmultidb.model.dto.UserDTO;
import com.example.todoappmultidb.service.UserService;

@Controller
public class UserWebController {
	private static final String MESSAGE = "message";
	@Autowired
	private UserService userService;

	@GetMapping("/")
	public String index(@RequestParam(required = false, defaultValue = "1") int db, Model model) {
		userService.setContext(db);
		List<UserDTO> allUser = userService.getAllUser();
		model.addAttribute("users", allUser);
		model.addAttribute(MESSAGE, allUser.isEmpty() ? "No user" : "");
		return "index";
	}

	@GetMapping("/user/edit/{id}")
	public String editUser(@RequestParam(required = false, defaultValue = "1") int db, @PathVariable long id,
			Model model) {
		userService.setContext(db);
		UserDTO user = userService.getUserById(id);
		model.addAttribute("user", user);
		model.addAttribute(MESSAGE, user == null ? "No user found with id: " + id : "");
		return "editUser";
	}

	@GetMapping("/user/new")
	public String newUser(Model model) {
		model.addAttribute("user", new UserDTO());
		model.addAttribute(MESSAGE, "");
		return "editUser";
	}

	@PostMapping("/user/save")
	public String saveUser(@RequestParam(required = false, defaultValue = "0") int db, UserDTO user) {
		if (db == 0)
			userService.setContext(1);
		else
			userService.setContext(db);

		final Long id = user.getId();
		if (id == null) {
			userService.insertNewUser(user);
		} else {
			userService.updateUserById(id, user);
		}
		return db == 0 ? "redirect:/" : "redirect:/?db=" + db;
	}

}
