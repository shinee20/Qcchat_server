package org.qucell.chat.controller;

import org.qucell.chat.model.user.LoginVO;
import org.qucell.chat.service.LoginService;
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



/**
 * Handles requests for the application home page.
 */
@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private LoginService loginService;

	@Autowired
	private UserService userService;


	@PostMapping("/login")
	public ResponseEntity login(@RequestBody LoginVO vo) {
		return new ResponseEntity<>(loginService.login(vo), HttpStatus.OK);
	}

	@PostMapping("")
	public ResponseEntity signUp(@RequestBody LoginVO vo){
		return new ResponseEntity<>(loginService.signUp(vo), HttpStatus.OK);
	}

	
	@Auth
	@GetMapping("/info")
	public ResponseEntity getUserInfo(@RequestHeader(required = false, defaultValue = "0") int idx){
		return new ResponseEntity<>(userService.getByUserId(idx), HttpStatus.OK);
	}

	@Auth
	@RequestMapping("/list")
	public ResponseEntity getFriendsList(@RequestHeader(required=false, defaultValue="0") int idx){
		return new ResponseEntity<>(userService.getAllFriendsList(idx), HttpStatus.OK);
	}
	/*
	 * redis test
	 */
//	@RequestMapping("/test")
//	public ResponseEntity getUsers() throws IOException {
//		return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
//	}
//
//	@RequestMapping("/test-refresh")
//	public ResponseEntity refreshUsers() throws IOException {
//		userService.removeCacheUsers();
//		return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
//	}
	/*
	 * redis test
	 */
}
