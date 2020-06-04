package org.qucell.chat.controller;

import java.io.IOException;

import org.qucell.chat.model.RoomVO;
import org.qucell.chat.service.RoomService;
import org.qucell.chat.service.UserService;
import org.qucell.chat.util.auth.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/chatroom")
public class ChatRoomController {
	
	@Autowired
	private UserService userService;
		
	@Autowired
	private RoomService roomService;
	
	/*
	 * add new chat room 
	 * @Param user name(owner)
	 * @Param room name, room password
	 */
	@Auth
	@PostMapping("/add")
	public ResponseEntity addChatRoom(
			@RequestHeader(required=false, defaultValue="0") int idx,
			@RequestBody RoomVO vo) throws IOException {
		return new ResponseEntity<>(roomService.insertChatRoom(idx, vo), HttpStatus.OK);
	}
}
