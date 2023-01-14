package com.example.todoappmultidb.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.todoappmultidb.model.User;
import com.example.todoappmultidb.repository.UserRepository;

@Transactional(readOnly = true)
@Service
public class UserService {

	private UserRepository userRepository;

	public List<User> getAllUser() {
		return userRepository.findAll();
	}

	public User getUserById(long id) {
		return userRepository.findById(id).orElse(null);
	}

	@Transactional(readOnly = false)
	public User insertNewUser(User userToSave) {
		userToSave.setId(null);
		return userRepository.save(userToSave);
	}

	@Transactional(readOnly = false)
	public User updateUserById(long id, User userToUpdate) {

		userToUpdate.setId(id);
		return userRepository.save(userToUpdate);
	}

}
