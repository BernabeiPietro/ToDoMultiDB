package com.example.todoappmultidb.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.todoappmultidb.model.User;

public interface UserRepository extends JpaRepository<User,Long> {

}
