package com.example.todoappmultidb.service;

import java.util.List;

import com.example.todoappmultidb.model.dto.ToDoDTO;

import javassist.NotFoundException;

public class ToDoService {
	private static final String TEMPORARY_IMPLEMENTATION = "Temporary implementation";

	public List<ToDoDTO> getAllToDo() throws NotFoundException {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	public ToDoDTO getToDoById(long l) throws NotFoundException {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	public ToDoDTO saveToDo(ToDoDTO todo) throws NotFoundException {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	public ToDoDTO updateToDo(long l, ToDoDTO todo) throws NotFoundException {

		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	public List<ToDoDTO> findByUserId(long l) throws NotFoundException {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}
}
