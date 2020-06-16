package org.qucell.chat.deprecated;
//package org.qucell.chat.controller;
//
//import java.io.IOException;
//
//import org.qucell.chat.model.room.RoomVO;
//import org.qucell.chat.service.RoomService;
//import org.qucell.chat.util.auth.Auth;
//import org.qucell.chat.util.request.RequestURL;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestHeader;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//@RestController
//@RequestMapping("/chatroom")
//public class ChatRoomController {
//
//	@Autowired
//	private RoomService roomService;
//	
//	/**
//	 * add new chat room 
//	 * @param idx
//	 * @param vo
//	 * @return
//	 * @throws IOException
//	 */
//	@Auth
//	@PostMapping("/add")
//	public ResponseEntity addChatRoom(@RequestHeader(required=false, defaultValue="0") int idx,
//									  @RequestBody RoomVO vo){
//		return new ResponseEntity<>(roomService.insertChatRoom(idx, vo), HttpStatus.OK);
//	}
//
//	/**
//	 * main view 
//	 * get all chat rooms 
//	 * @param jwt
//	 * @return
//	 * @throws Exception 
//	 */
//	@GetMapping("/")
//	public ResponseEntity getAllRooms(@RequestHeader(value = "Authorization", required = false) String jwt, 
//			@RequestURL String requestURL) throws Exception {
//		return new ResponseEntity<>(roomService.getAllRooms(jwt, requestURL), HttpStatus.OK);
//	}
//	
//	/**
//	 * get attending the rooms
//	 * @param idx
//	 * @return
//	 */
//	@Auth
//	@GetMapping("/list")
//	public ResponseEntity getUserRoooms(@RequestHeader(required = false, defaultValue = "0") int idx)
//	{
//		return new ResponseEntity<>(roomService.getUserRooms(idx), HttpStatus.OK);
//	}
//	
//	/**
//	 * edit room password 
//	 * - already created room 
//	 * - only owner do this method 
//	 * @param jwt
//	 * @param roomName
//	 * @return
//	 */
//	@Auth
//	@PutMapping("/edit")
//	public ResponseEntity editPassword(@RequestHeader(required = false, defaultValue = "0") int idx, 
//									   @RequestBody RoomVO vo) {
//		return new ResponseEntity<>(roomService.editPassword(idx,vo), HttpStatus.OK);
//	}
//	
//	/**
//	 * add friends at chat room 
//	 * @param idx
//	 * @param vo
//	 * @return
//	 */
//	@Auth
//	@PutMapping("/addFriends")
//	public ResponseEntity addFriendsAtRoom(@RequestHeader(required=false, defaultValue="0") int idx,
//										   @RequestBody RoomVO vo) {
//		return new ResponseEntity<>(roomService.addFriends(idx, vo), HttpStatus.OK);
//	}
//	
//	/**
//	 * get user list at chat room 
//	 * @param idx
//	 * @param roomName
//	 * @return
//	 */
//	@Auth
//	@GetMapping("/list/{roomName}")
//	public ResponseEntity getUserListAtRoom(@RequestHeader(required=false, defaultValue="0") int idx,
//											@PathVariable(value="roomName") final String roomName) {
//		return new ResponseEntity<>(roomService.getUserList(roomName), HttpStatus.OK);
//	}
//}
