package com.example.todoappmultidb.webcontroller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ErrorWebController {

	private static final String MESSAGE = "message";

	@GetMapping(value = { "/error/", "/error/{error}" })
	public String getErrorPage(@PathVariable(name = "error", required = false) String message, Model model) {
		if (message == null)
			message = "Generic Error";
		model.addAttribute(MESSAGE, message);
		return "errorPage";
	}
	
}
