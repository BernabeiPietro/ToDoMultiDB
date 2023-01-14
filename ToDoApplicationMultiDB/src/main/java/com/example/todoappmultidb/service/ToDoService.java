package com.example.todoappmultidb.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.todoappmultidb.model.ToDo;
import com.example.todoappmultidb.repository.ToDoRepository;

@Transactional(readOnly = true)
@Service
public class ToDoService {

	ToDoRepository toDoRepository;

	public List<ToDo> findAll() {
		return toDoRepository.findAll();
	}

	public ToDo findById(Long id) {
		return toDoRepository.findById(id).orElse(null);
	}
	@Transactional(readOnly = false)
	public ToDo save(ToDo toSave) {
		return toDoRepository.save(toSave);
	}

	@Transactional(readOnly = false)
	public ToDo updateById(long id, ToDo toUpdate) {
		toUpdate.setId(id);
		return toDoRepository.save(toUpdate);
	}

}
