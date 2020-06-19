package org.qucell.chat.controller;

import java.util.Map;

import org.qucell.chat.util.request.RequestURL;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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

	@Value("${websocket.port}")
	private String websocketPort;

	@GetMapping("/")
	public String index(@RequestURL String requestURL, 
			Map<String, Object> model) {
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
	
//	@GetMapping("/") 
//	public String login() {
//		return "login";
//	}
	
//	@ResponseBody
//	@PostMapping("/login")
//	public ResponseEntity login(@RequestBody LoginVO vo) {
//		//register at channel 
//		return new ResponseEntity<>(loginService.login(vo), HttpStatus.OK);
//	}
//
//	@ResponseBody
//	@PostMapping("/signup")
//	public ResponseEntity signUp(@RequestBody LoginVO vo){
//		return new ResponseEntity<>(loginService.signUp(vo), HttpStatus.OK);
//	}

//	
//	@Auth
//	@ResponseBody
//	@GetMapping("/info")
//	public ResponseEntity getUserInfo(@RequestHeader(required = false, defaultValue = "0") int idx){
//		return new ResponseEntity<>(userService.getByUserId(idx), HttpStatus.OK);
//	}
//
//	@Auth
//	@RequestMapping("/list")
//	public ResponseEntity getFriendsList(@RequestHeader(required=false, defaultValue="0") int idx){
//		return new ResponseEntity<>(userService.getAllFriendsList(idx), HttpStatus.OK);
//	}

}
