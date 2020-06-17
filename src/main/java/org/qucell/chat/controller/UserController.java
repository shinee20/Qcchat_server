package org.qucell.chat.controller;

import java.util.Map;

import org.qucell.chat.model.user.LoginVO;
import org.qucell.chat.netty.server.NettyServer;
import org.qucell.chat.netty.server.handler.LoginHandler;
import org.qucell.chat.service.LoginService;
import org.qucell.chat.service.UserService;
import org.qucell.chat.util.auth.Auth;
import org.qucell.chat.util.request.RequestURL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;

/**
 * Handles requests for the application home page.
 */
/**
 * updated 20/06/15
 * @author myseo
 */
@Slf4j
@Controller
public class UserController {

	@Autowired
	private LoginService loginService;

	@Autowired
	private UserService userService;

	@Value("${websocket.port}")
	private String websocketPort;

	
	@GetMapping("/")
	public String index(@RequestURL String requestURL, Map<String, Object> model) {
		/**
		 * parsing request url = http://localhost:10101/
		 */
		int pos = 0;
		if (requestURL.startsWith("http://")) {
			pos = "http://".length();
		}
		else  {
			pos = "https://".length();
		}
		
		int pos2 = requestURL.indexOf(":", pos);
		if (pos2 < 0) {
			pos2 = requestURL.length();
		}
		
		int pos3 = requestURL.indexOf("/", pos);
		if (pos3 < 0) {
			pos3 = requestURL.length();
		}
		
		pos2 = Math.min(pos2, pos3);
		
		String host = requestURL.substring(pos, pos2);
		/**
		 * parsing request url
		 */
		
		model.put("host", host);
		model.put("websocketPort", websocketPort);
		
		
		return "index";
	}
	
	@ResponseBody
	@PostMapping("/login")
	public ResponseEntity login(@RequestBody LoginVO vo) {
		//register at channel 
		return new ResponseEntity<>(loginService.login(vo), HttpStatus.OK);
	}

	@ResponseBody
	@PostMapping("/signup")
	public ResponseEntity signUp(@RequestBody LoginVO vo){
		return new ResponseEntity<>(loginService.signUp(vo), HttpStatus.OK);
	}

	
	@Auth
	@ResponseBody
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
