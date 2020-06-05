package org.qucell.chat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

	//use temporary -> redirecting
	@GetMapping("/")
	public String login() {
		return "login";
	}
}
