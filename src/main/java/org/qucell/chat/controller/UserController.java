package org.qucell.chat.controller;

import java.io.IOException;

import org.qucell.chat.model.LoginVO;
import org.qucell.chat.service.JwtService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;


/**
 * Handles requests for the application home page.
 */
@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private LoginService loginService;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private UserService userService;

	/**
	 * Simply selects the home view to render by returning its name.
	 */

	@PostMapping("/login")
	public ResponseEntity login(@RequestBody LoginVO vo) throws IOException {
		log.info(vo.toString());
		return new ResponseEntity<>(loginService.login(vo), HttpStatus.OK);
	}

	@PostMapping("")
	public ResponseEntity signUp(@RequestBody LoginVO vo){
		return new ResponseEntity<>(loginService.signUp(vo), HttpStatus.OK);
	}
	
//	@RequestMapping(value="/logout", method=RequestMethod.POST)
//	public @ResponseBody DefaultRes logout(HttpServletResponse response) {
//		setCookie(response, ""); //remove from jwt
//		return DefaultRes.res(StatusCode.OK, ResponseMessage.LOGOUT);	
//	}

	/*
	 * @param : user_name
	 */
	@Auth
	@GetMapping("/info")
	public ResponseEntity getUserInfo(@RequestHeader(required=false, defaultValue="0") int idx) throws IOException {
		return new ResponseEntity<>(userService.getByUserId(idx), HttpStatus.OK);
	}

//	public void setCookie(HttpServletResponse response, String token) {
//		Cookie cookie = new Cookie("Authorization", token);
//		//1년으로 설정
//		cookie.setMaxAge(365*24*60*60);
//		cookie.setPath("/");
//		response.addCookie(cookie);
//	}
	
	@Auth
	@RequestMapping("/list")
	public ResponseEntity getFriendsList(@RequestHeader(required=false, defaultValue="0") int idx) throws IOException {
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
