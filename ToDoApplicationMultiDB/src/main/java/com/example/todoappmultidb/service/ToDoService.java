package com.example.todoappmultidb.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.todoappmultidb.model.ToDo;
import com.example.todoappmultidb.model.User;
import com.example.todoappmultidb.model.dto.ToDoDTO;
import com.example.todoappmultidb.repository.ToDoRepository;
import com.example.todoappmultidb.repository.UserRepository;

import javassist.NotFoundException;

@Transactional(readOnly = true)
@Service
public class ToDoService {

	@Autowired
	ToDoRepository toDoRepository;
	@Autowired
	UserService userService;

	@Transactional(rollbackFor = NotFoundException.class)
	public List<ToDoDTO> findAll() throws NotFoundException {
		List<ToDo> todoFound = toDoRepository.findAll();
		if (todoFound.isEmpty())
			throw new NotFoundException("Not found any ToDo");
		return todoFound.stream().map(x->toDTO(x)).collect(Collectors.toList());
	}

	@Transactional(rollbackFor = NotFoundException.class)
	public ToDoDTO findByIdDTO(Long id) throws NotFoundException {
		return toDTO(findById(id));
	}

	protected ToDo findById(Long id) throws NotFoundException {
		return toDoRepository.findById(id).orElseThrow(() -> new NotFoundException("Not found any ToDo"));
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = IllegalArgumentException.class)
	public ToDoDTO save(ToDoDTO toDoDTO) throws NotFoundException {
		verifyNullElement(toDoDTO);
		
		User retrieved = userService.getUser(toDoDTO.getIdOfUser());
		ToDo toSave=new ToDo(toDoDTO,retrieved);
		return toDTO(toDoRepository.save(toSave));

	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = IllegalArgumentException.class)
	public ToDoDTO updateById(long id, ToDoDTO toUpdate) throws NotFoundException {
		verifyNullElement(toUpdate);
		toUpdate.setId(id);
		ToDo retrieve=findById(toUpdate.getIdOfUser());
		retrieve.setLocalDateTime(toUpdate.getDate());
		retrieve.setToDo(toUpdate.getToDo());
		return toDTO(toDoRepository.save(retrieve));
	}

	private void verifyNullElement(ToDoDTO toDoDTO) {
		if (toDoDTO.equals(new ToDoDTO(null, null, null, null)))
			throw new IllegalArgumentException("ToDo with null property");
	}
	
	public ToDoDTO toDTO(ToDo t)
	{
		return new ToDoDTO(t);
	}
	
	
}
