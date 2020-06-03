package org.qucell.chat.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.qucell.chat.model.DefaultRes;
import org.qucell.chat.model.Users;
import org.qucell.chat.service.JwtService;
import org.qucell.chat.service.LoginService;
import org.qucell.chat.service.UserService;
import org.qucell.chat.util.ResponseMessage;
import org.qucell.chat.util.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * Handles requests for the application home page.
 */
@RestController
@RequestMapping("/users")
public class UserController {
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private LoginService loginService;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private UserService userService;
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public @ResponseBody DefaultRes login(HttpServletResponse response,
			@RequestParam String username)  {
		try {
			Users loginUser = loginService.login(username);
			logger.info(loginUser.toString());
			String token = jwtService.create("user", loginUser, "user");
			logger.info(token);
			setCookie(response, token);
//			response.setHeader("Authorization", token);
			return DefaultRes.res(StatusCode.OK, ResponseMessage.LOGIN, loginUser);
		}
		catch(Exception e) {
			return DefaultRes.res(StatusCode.NO_CONTENT, ResponseMessage.NOT_FOUND_USER);
		}
		
	}
	
	@RequestMapping(value="/logout", method=RequestMethod.POST)
	public @ResponseBody DefaultRes logout(HttpServletResponse response) {
		setCookie(response, "");
		return DefaultRes.res(StatusCode.OK, ResponseMessage.LOGOUT);	
	}
	
	/*
	 * @param : user_name
	 */
	@RequestMapping(value="/info", method=RequestMethod.GET)
	public ResponseEntity getUserInfo(HttpSession session, @RequestParam String userName) throws IOException {
		return new ResponseEntity<>(userService.getByUserName(userName), HttpStatus.OK);
	}
	
	public void setCookie(HttpServletResponse response, String token) {
		Cookie cookie = new Cookie("Authorization", token);
	    //1년으로 설정
		cookie.setMaxAge(365*24*60*60);
		cookie.setPath("/");
	    response.addCookie(cookie);
	}
	
	/*
	 * redis test
	 */
	@RequestMapping("/test")
	public ResponseEntity getUsers() throws IOException {
		return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
	}
	
	@RequestMapping("/test-refresh")
	public ResponseEntity refreshUsers() throws IOException {
		userService.removeCacheUsers();
		return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
	}
	/*
	 * redis test
	 */
}
