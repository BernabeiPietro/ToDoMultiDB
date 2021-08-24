package com.example.todoappmultidb.rest;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.todoappmultidb.model.User;
import com.example.todoappmultidb.service.UserService;

import javassist.NotFoundException;

import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/users")
public class UserRestController {
	@Autowired
	private UserService userService;

	@GetMapping
	public List<User> getUsers() {
		try {
			return userService.getAllUser();

		} catch (NotFoundException e) {

			throw new ResponseStatusException(HttpStatus.NO_CONTENT, e.getMessage());
		}
	}

	@GetMapping("/{id}")
	public User getUserById(@PathVariable long id) {
		try {
		return userService.getUserById(id);

		} catch (NotFoundException e) {

		throw new ResponseStatusException(HttpStatus.NO_CONTENT, e.getMessage());
		}
	}

	@PostMapping("/new")
	@ResponseStatus(HttpStatus.CREATED)
	public User saveUser(@RequestBody User newUser) {
		try {
			return userService.insertNewUser(newUser);
		} catch (Exception e) {
			// TODO: handle exception
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
	}

	@PutMapping("/update//{id}")
	public User updateUser(@PathVariable long id, @RequestBody User newUser) {

		try {
			return userService.updateUser(id, newUser);
		} catch (IllegalArgumentException e) {

			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		} catch (NotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}
}
