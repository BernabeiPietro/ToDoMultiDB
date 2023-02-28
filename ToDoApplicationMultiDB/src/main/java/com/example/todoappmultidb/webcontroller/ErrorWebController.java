package com.example.todoappmultidb.webcontroller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorWebController{

	private static final String MESSAGE = "message";

	@GetMapping(value = { "/error/", "/error/{error}" })
	public String getErrorPath(@PathVariable(name = "error", required = false) String message, Model model) {
		if (message == null)
			message = "Generic Error";
		model.addAttribute(MESSAGE, message);
		return "errorPage";
	}

	

}
