package com.example.todoappmultidb.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.todoappmultidb.model.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

}

