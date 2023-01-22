package com.example.todoappmultidb.service;


	import java.util.List;

import org.springframework.stereotype.Service;

	import com.example.todoappmultidb.model.dto.ToDoDTO;
	import com.example.todoappmultidb.model.dto.UserDTO;

	import javassist.NotFoundException;

	@Service
	public class ToDoDTOService {
		private static final String TEMPORARY_IMPLEMENTATION = "Temporary implementation";

		public List<ToDoDTO> getAllToDo() throws NotFoundException {
			throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
		}

		public ToDoDTO getToDoById(long l) throws NotFoundException {
			throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
		}

		public ToDoDTO saveToDo(ToDoDTO todo) {
			throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
		}

		public ToDoDTO updateToDo(long l, ToDoDTO todo)throws IllegalArgumentException,NotFoundException {
			
			throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
		}
		public List<ToDoDTO> findByUserId(UserDTO user)throws NotFoundException{
			throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
		}	
}
