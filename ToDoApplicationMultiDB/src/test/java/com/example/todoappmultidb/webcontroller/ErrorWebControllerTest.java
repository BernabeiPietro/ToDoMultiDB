package com.example.todoappmultidb.webcontroller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = ErrorWebController.class)
public class ErrorWebControllerTest {
	private final String MESSAGE = "message";

	@Autowired
	private MockMvc mvc;

	@Test
	public void test_getError_emptyMessage() throws Exception {
		mvc.perform(get("/error")).andExpect(view().name("errorPage"))
				.andExpect(model().attribute(MESSAGE, "Generic Error"));
	}


}
