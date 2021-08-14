package com.example.todoappmultidb.repository;

import java.util.List;
import java.util.Optional;

import com.example.todoappmultidb.model.ToDo;

public class ToDoRepository {
	private static final String TEMPORARY_IMPLEMENTATION = "Temporary implementation";

	public List<ToDo> findAll() {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	public Optional<ToDo> findById(Long id) {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	public ToDo save(ToDo toSave) {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	public ToDo update(ToDo toUpdate) {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

}
