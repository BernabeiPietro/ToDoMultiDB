package com.example.todoappmultidb.webcontroller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ErrorWebController implements ErrorController {

	private static final String MESSAGE = "message";

	@GetMapping(value = { "/error", "/error/{message}" })
	public String getErrorPath(@PathVariable(required = false) String message, Model model) {
		if (message != null)
			model.addAttribute(MESSAGE, message);
		else
			model.addAttribute(MESSAGE, "Generic Error");
		return "errorPage";
	}

	@Override
	public String getErrorPath() {
		return "/error";
	}

}
