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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.todoappmultidb.model.dto.ToDoDTO;
import com.example.todoappmultidb.model.dto.UserDTO;
import com.example.todoappmultidb.service.ToDoService;
import com.example.todoappmultidb.service.UserService;

import javassist.NotFoundException;

@RestController
@RequestMapping("/api/todo")
public class ToDoRestController {

	@Autowired
	private ToDoService todoService;
	@Autowired
	private UserService userService;
	@GetMapping("/{db}")
	public List<ToDoDTO> getToDo(@PathVariable int db) {
		userService.setContext(db);
		try {
			return todoService.findAll();

		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NO_CONTENT, e.getMessage());

		}

	}

	@GetMapping("/{db}/id/{id}")
	public ToDoDTO getToDoById(@PathVariable int db,@PathVariable long id) {
		userService.setContext(db);
		try {
			return todoService.findByIdDTO(id);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NO_CONTENT, e.getMessage());
		}

	}

	/* Not developed the ToDo Part
	 * @GetMapping("/ofuser/{id}") public List<ToDoDTO>
	 * getToDoByUserId(@PathVariable Long id) { try { return
	 * todoService.findByUserId(new UserDTO(id, null, null));
	 * 
	 * } catch (NotFoundException e) { throw new
	 * ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage()); } }
	 */

	@PostMapping("/{db}/new")
	@ResponseStatus(HttpStatus.CREATED)
	public ToDoDTO save(@PathVariable int db, @RequestBody ToDoDTO todo) {
		userService.setContext(db);
		try {
			return todoService.save(todo);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
	}

	@PutMapping("/{db}/update/{id}")
	public ToDoDTO updateToDo(@RequestBody ToDoDTO todo, @PathVariable int db,@PathVariable Long id) {
		userService.setContext(db);
		try {
			return todoService.updateById(id, todo);
		} catch (IllegalArgumentException e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		} catch (NotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}
}
