package com.example.todoappmultidb.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.todoappmultidb.model.ToDo;


public interface ToDoRepository extends JpaRepository<ToDo,Long> {

	@Query("Select td from ToDo td where td.date = :data")
	List<ToDo> findAllToDoWithDate(@Param("data") LocalDateTime date);
}