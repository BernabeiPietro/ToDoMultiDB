package com.example.todoappmultidb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.todoappmultidb.model.ToDo;
import com.example.todoappmultidb.repository.ToDoRepository;

import javassist.NotFoundException;

@Transactional(readOnly = true)
@Service
public class ToDoService {

	@Autowired
	ToDoRepository toDoRepository;

	public List<ToDo> findAll() throws NotFoundException {
		List<ToDo> todoFound = toDoRepository.findAll();
		if(todoFound.isEmpty())
			throw new NotFoundException("Not found any ToDo");
		return todoFound;
	}

	public ToDo findById(Long id) throws NotFoundException {
		return toDoRepository.findById(id).orElseThrow(()->new NotFoundException("Not found any ToDo"));
	}
	@Transactional(readOnly = false)
	public ToDo save(ToDo toSave) {
		toSave.equals(new ToDo(null,null,null,null));
		return toDoRepository.save(toSave);
		
	}

	@Transactional(readOnly = false)
	public ToDo updateById(long id, ToDo toUpdate) {
		toUpdate.equals(new ToDo(null,null,null,null));
		toUpdate.setId(id);
		return toDoRepository.save(toUpdate);
	}

}
