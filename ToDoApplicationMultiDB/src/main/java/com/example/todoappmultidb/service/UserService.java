package com.example.todoappmultidb.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.todoappmultidb.dto.UserDTO;
import com.example.todoappmultidb.model.User;

@Service
public class UserService {
	private static final String TEMPORARY_IMPLEMENTATION = "Temporary implementation";

	public List<UserDTO> getAllUser() {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	public UserDTO getUserById(long l) {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	public void insertNewUser(UserDTO userDTO) {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);

	}

	public void updateUserById(long l, UserDTO userDTO) {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
		
	}
}
