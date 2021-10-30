package com.example.todoappmultidb;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Optional;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.todoappmultidb.model.User;
import com.example.todoappmultidb.repository.UserRepository;
import com.example.todoappmultidb.routing.DataSourceContextHolder;
import com.example.todoappmultidb.routing.config.DataSourceEnum;
import com.example.todoappmultidb.routing.config.DataSourceRoutingConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserRepositoryRoutingIT {

	@Autowired
	UserRepository userRepository;

	@Test
	void saveUserOnTwoDifferentDB_test() {
		DataSourceContextHolder.set(DataSourceEnum.DATASOURCE_ONE);
		User saved = userRepository.save(new User(null, new ArrayList<>(), "db1", "db1"));
		Optional<User> findById = userRepository.findById(saved.getId());
		assertTrue(findById.isPresent());
		assertThat(findById.get()).hasFieldOrPropertyWithValue("id", saved.getId())
				.hasFieldOrPropertyWithValue("email", saved.getEmail())
				.hasFieldOrPropertyWithValue("name", saved.getName());
		DataSourceContextHolder.set(DataSourceEnum.DATASOURCE_TWO);
		assertThat(userRepository.findById(saved.getId())).isEmpty();
	}
}
