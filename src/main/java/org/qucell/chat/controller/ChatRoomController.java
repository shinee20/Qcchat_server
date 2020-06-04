package org.qucell.chat.controller;

import java.io.IOException;

import org.qucell.chat.model.RoomVO;
import org.qucell.chat.service.RoomService;
import org.qucell.chat.service.UserService;
import org.qucell.chat.util.auth.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/chatroom")
public class ChatRoomController {
	
	@Autowired
	private UserService userService;
		
	@Autowired
	private RoomService roomService;
	
	/**
	 * add new chat room 
	 * @param idx
	 * @param vo
	 * @return
	 * @throws IOException
	 */
	@Auth
	@PostMapping("/add")
	public ResponseEntity addChatRoom(
			@RequestHeader(required=false, defaultValue="0") int idx,
			@RequestBody RoomVO vo) throws IOException {
		return new ResponseEntity<>(roomService.insertChatRoom(idx, vo), HttpStatus.OK);
	}

	/**
	 * main view 
	 * get all chat rooms 
	 * @param jwt
	 * @return
	 */
	@GetMapping("")
	public ResponseEntity getAllRooms(@RequestHeader(value = "Authorization", required = false) String jwt) {
		return new ResponseEntity<>(roomService.getAllRooms(jwt), HttpStatus.OK);
	}
	
	/**
	 * get attending the rooms
	 * @param idx
	 * @return
	 */
	@Auth
	@GetMapping("/list")
	public ResponseEntity getUserRoooms(@RequestHeader(required = false, defaultValue = "0") int idx)
	{
		log.info("get user attending room.. user =>" + idx);
		return new ResponseEntity<>(roomService.getUserRooms(idx), HttpStatus.OK);
	}
}
