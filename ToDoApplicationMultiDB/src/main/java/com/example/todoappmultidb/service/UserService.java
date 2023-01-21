package com.example.todoappmultidb.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.todoappmultidb.model.dto.UserDTO;

import javassist.NotFoundException;

@Service
public class UserService {

	private static final String TEMPORARY_IMPLEMENTATION = "Temporary implementation";

	public List<UserDTO> getAllUser() throws NotFoundException{
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	public UserDTO getUserById(long anyLong)throws NotFoundException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	public UserDTO insertNewUser(UserDTO u) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	public UserDTO updateUser(long id, UserDTO updated) throws IllegalArgumentException,NotFoundException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

}
