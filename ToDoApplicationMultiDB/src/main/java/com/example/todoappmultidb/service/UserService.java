package com.example.todoappmultidb.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.todoappmultidb.model.User;
import com.example.todoappmultidb.model.dto.ToDoDTO;
import com.example.todoappmultidb.model.dto.UserDTO;
import com.example.todoappmultidb.repository.UserRepository;
import com.example.todoappmultidb.routing.DataSourceContextHolder;
import com.example.todoappmultidb.routing.config.DataSourceEnum;

import javassist.NotFoundException;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private DataSourceContextHolder dataContext;

	@Transactional(rollbackFor = NotFoundException.class)
	public List<UserDTO> getAllUser() throws NotFoundException {

		List<User> userFound = userRepository.findAll();
		if (userFound.isEmpty())
			throw new NotFoundException("Not found any User");
		return userFound.stream().map(this::toDTO).collect(Collectors.toList());
	}

	@Transactional(rollbackFor = NotFoundException.class)
	public UserDTO getUserById(long id) throws NotFoundException {
		return toDTO(getUser(id));
	}

	protected User getUser(long id) throws NotFoundException {
		return userRepository.findById(id).orElseThrow(() -> new NotFoundException("Not found any User"));
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = {
			IllegalArgumentException.class, NullPointerException.class })
	public UserDTO insertNewUser(UserDTO userToSave) {
		userToSave.setId(null);
		verifyEmptyField(userToSave);
		return toDTO(userRepository.save(new User(userToSave)));
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = {
			IllegalArgumentException.class, NotFoundException.class, NullPointerException.class })
	public UserDTO updateUserById(long id, UserDTO userToUpdate) throws NotFoundException {
		verifyEmptyField(userToUpdate);
		userToUpdate.setId(id);
		User retrieved = this.getUser(id);
		retrieved.setEmail(userToUpdate.getEmail());
		retrieved.setName(userToUpdate.getName());
		return toDTO(userRepository.save(retrieved));
	}

	@Transactional(rollbackFor = NotFoundException.class)
	public List<ToDoDTO> getToDoOfUser(long id) throws NotFoundException {
		User retrieved = getUser(id);
		return retrieved.getToDo().stream().map(ToDoDTO::new).distinct().collect(Collectors.toList());
	}

	public DataSourceEnum setDatabase(int ctx) {
		return dataContext.setDatabase(ctx);
	}

	public void clearContext() {
		dataContext.clear();
	}

	private void verifyEmptyField(UserDTO userToSave) {
		if (userToSave.getEmail().isEmpty() || userToSave.getName().isEmpty())
			throw new IllegalArgumentException("User with empty fields");
	}

	public UserDTO toDTO(User user) {
		return new UserDTO(user);
	}

}
