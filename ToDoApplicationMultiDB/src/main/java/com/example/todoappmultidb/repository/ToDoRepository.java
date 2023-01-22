package com.example.todoappmultidb.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.todoappmultidb.model.ToDo;
import com.example.todoappmultidb.model.User;

@Repository
public interface ToDoRepository extends JpaRepository<ToDo,Long> {

	@Query("Select todo from ToDo todo where todo.idOfUser = :id")
	List<ToDo> findToDoByUserId(@Param("id") User u);
}
