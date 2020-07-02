package org.qucell.chat.controller;

import org.qucell.chat.model.DefaultRes;
import org.qucell.chat.model.user.LoginRes;
import org.qucell.chat.model.user.LoginVO;
import org.qucell.chat.service.JwtService.TokenRes;
import org.qucell.chat.service.LoginService;
import org.qucell.chat.util.request.RequestURL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

	@Value("${websocket.port}")
	private String websocketPort;
	
	@Autowired
	private LoginService loginService;
	
	private String parsingURL(String requestURL) {
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
		return host;
	}
//	@RequestMapping(value="/", method=RequestMethod.GET)
//	public String index(@RequestURL String requestURL, 
//			Map<String, Object> model) {
//		/**
//		 * parsing request url = http://localhost:10101/
//		 */
//		int pos = 0;
//		if (requestURL.startsWith("http://")) {
//			pos = "http://".length();
//		}
//		else  {
//			pos = "https://".length();
//		}
//		
//		int pos2 = requestURL.indexOf(":", pos);
//		if (pos2 < 0) {
//			pos2 = requestURL.length();
//		}
//		
//		int pos3 = requestURL.indexOf("/", pos);
//		if (pos3 < 0) {
//			pos3 = requestURL.length();
//		}
//		
//		pos2 = Math.min(pos2, pos3);
//		
//		String host = requestURL.substring(pos, pos2);
//		/**
//		 * parsing request url
//		 */
//
//		model.put("host", host);
//		model.put("websocketPort", websocketPort);
//		
//		return "room";
//	}
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String goToRoom() {
		return "room";
	}

	@ResponseBody
	@RequestMapping(value ="/login", method=RequestMethod.POST)
	public ResponseEntity login(@RequestURL String requestURL, @RequestBody LoginVO vo) {
		log.info(requestURL);
		String host = parsingURL(requestURL);
		vo.setHost(host);
		vo.setWebsocketPort(websocketPort);
		return new ResponseEntity<>(loginService.login(vo), HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping(value ="/signup", method=RequestMethod.POST)
	public ResponseEntity signup(@RequestBody LoginVO vo) {
		log.info(vo.toString());
		return new ResponseEntity<>(loginService.signUp(vo), HttpStatus.OK);
	}
	
}
