package org.qucell.chat.controller;

import org.qucell.chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/chatroom")
public class ChatRoomController {
	
	@Autowired
	private UserService userService;
		
	
	
	
}
