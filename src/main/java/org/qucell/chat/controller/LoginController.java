package org.qucell.chat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	//use test
	@GetMapping("/")
	public String login() {
		return "login";
	}
}
