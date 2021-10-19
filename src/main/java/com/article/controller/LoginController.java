package com.article.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {

	@RequestMapping(value = "/signin", method = RequestMethod.GET)
	public String login(Model model) {
		model.addAttribute("title", "Sign in | Newz Article");
		return "signin";
	}
}
