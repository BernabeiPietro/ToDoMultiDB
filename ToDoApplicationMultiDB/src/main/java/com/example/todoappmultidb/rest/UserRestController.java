package com.example.todoappmultidb.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.todoappmultidb.model.dto.UserDTO;
import com.example.todoappmultidb.service.UserService;

import javassist.NotFoundException;

@RestController
@RequestMapping("/api/users")
public class UserRestController {
	@Autowired
	private UserService userService;

	@GetMapping("/{db}")
	public List<UserDTO> getUsers(@PathVariable int db) {
		userService.setContext(db);
		try {
			return userService.getAllUser();

		} catch (NotFoundException e) {

			throw new ResponseStatusException(HttpStatus.NO_CONTENT, e.getMessage());
		}
		catch (NullPointerException e) {
			throw new ResponseStatusException(HttpStatus.NO_CONTENT, e.getMessage());
		}
	}

	@GetMapping("/{db}/id/{id}")
	public UserDTO getUserById(@PathVariable int db, @PathVariable long id) {
		userService.setContext(db);
		try {
			return userService.getUserById(id);
			

		} catch (NotFoundException e) {

			throw new ResponseStatusException(HttpStatus.NO_CONTENT, e.getMessage());
		}
	}

	@PostMapping("/{db}/new")
	@ResponseStatus(HttpStatus.CREATED)
	public UserDTO saveUser(@PathVariable int db, @RequestBody UserDTO newUser) {
		userService.setContext(db);
		try {
			return userService.insertNewUser(newUser);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
	}

	@PutMapping("/{db}/update/{id}")
	public UserDTO updateUser(@PathVariable int db, @PathVariable long id, @RequestBody UserDTO newUser) {
		userService.setContext(db);
		try {
			return userService.updateUserById(id, newUser);
		} catch (IllegalArgumentException e) {

			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		} catch (NotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}
}
