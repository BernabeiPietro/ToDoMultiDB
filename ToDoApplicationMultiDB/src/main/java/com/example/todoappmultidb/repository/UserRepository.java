package com.example.todoappmultidb.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.todoappmultidb.model.User;


@Repository
public class UserRepository {

	private static final String TEMPORARY_IMPLEMENTATION = "Temporary implementation";

	public List<User> findAll() {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	public Optional<User> findById(long l) {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	public User save(User user) {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	
	}
}