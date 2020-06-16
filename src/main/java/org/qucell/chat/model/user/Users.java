package org.qucell.chat.model.user;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Users{
//user dto 
	private int userId;
	private String userName;
	private Date regDate;
	private Date updateDate;
	private List<Users> friendList;
	
}
