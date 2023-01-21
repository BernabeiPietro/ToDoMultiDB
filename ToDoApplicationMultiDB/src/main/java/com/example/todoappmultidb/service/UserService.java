package com.example.todoappmultidb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.todoappmultidb.model.User;
import com.example.todoappmultidb.repository.UserRepository;
import com.example.todoappmultidb.routing.DataSourceContextHolder;
import com.example.todoappmultidb.routing.config.DataSourceEnum;

import javassist.NotFoundException;

@Transactional(readOnly = true)
@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private DataSourceContextHolder dataContext;

	@Transactional(rollbackFor=NotFoundException.class)
	public List<User> getAllUser() throws NotFoundException {

		List<User> userFound = userRepository.findAll();
		if (userFound.isEmpty())
			throw new NotFoundException("Not found any User");
		return userFound;
	}

	@Transactional(rollbackFor=NotFoundException.class)
	public User getUserById(long id) throws NotFoundException {
		return userRepository.findById(id).orElseThrow(() -> new NotFoundException("Not found any User"));
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = IllegalArgumentException.class)
	public User insertNewUser(User userToSave) {
		userToSave.setId(null);
		verifyNullValue(userToSave);
		return userRepository.save(userToSave);
	}

	@Transactional(readOnly = false, propagation=Propagation.REQUIRES_NEW, rollbackFor = IllegalArgumentException.class)
	public User updateUserById(long id, User userToUpdate) {
		verifyNullValue(userToUpdate);
		userToUpdate.setId(id);

		return userRepository.save(userToUpdate);
	}

	public DataSourceEnum setContext(int ctx) {
		dataContext.set(DataSourceEnum.values()[ctx - 1]);
		return dataContext.getDataSource();
	}

	private void verifyNullValue(User userToSave) {
		if (userToSave.equals(new User(null, null, null, null)))
			throw new IllegalArgumentException("User with null property");
	}
}
