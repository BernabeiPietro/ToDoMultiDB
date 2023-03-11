package com.example.todoappmultidb.webcontroller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorWebController implements ErrorController {

	private static final String MESSAGE = "message";

	@GetMapping(value = { "/error" })
	public String getErrorPath(Model model) {
		model.addAttribute(MESSAGE, "Generic Error");
		return "errorPage";
	}

	@Override
	public String getErrorPath() {
		return "/error";
	}

}
