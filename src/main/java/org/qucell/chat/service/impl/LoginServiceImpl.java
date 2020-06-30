package org.qucell.chat.service.impl;

import org.qucell.chat.dao.UserDao;
import org.qucell.chat.model.DefaultRes;
import org.qucell.chat.model.user.LoginRes;
import org.qucell.chat.model.user.LoginVO;
import org.qucell.chat.model.user.Users;
import org.qucell.chat.service.JwtService;
import org.qucell.chat.service.LoginService;
import org.qucell.chat.service.RedisService;
import org.qucell.chat.util.ResponseMessage;
import org.qucell.chat.util.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService{

	@Autowired
	private UserDao userDao;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private RedisService redisService;
	
	//login 
//	@Override
//	public Users login(LoginVO vo) {
//		log.info(vo.getUserName());
//		Users user = userDao.getByUserName(vo.getUserName());
//		if (user != null) {//check already login 
//			//create token 
//			final JwtService.TokenRes tokenDto = new JwtService.TokenRes(jwtService.create(user.getUserId()));
//			log.info("success login " + tokenDto.toString());
//
//			/*
//			 * save at redis cache
//			 */
//			//save user info
//			String key = "id:"+user.getUserId();
//			redisService.saveValue(key, user);
//			
//			log.info("--------------------redis dao save--------------------" + redisService.getValue(key));
//			
//			/*
//			 * save at redis cache 
//			 */	
//			return user;
//		}
//		return null;
//		
//	}

	@Override
	public DefaultRes<LoginRes> login(LoginVO vo) {
		log.info(vo.toString());
		Users user = userDao.getByUserName(vo.getUserName());
		if (user != null) {//check already login 
			//create token 
			final JwtService.TokenRes tokenDto = new JwtService.TokenRes(jwtService.create(user.getUserId()));
			log.info("success login " + tokenDto.toString());

			/*
			 * save at redis cache
			 */
			//save user info
			String key = "id:"+user.getUserId();
			redisService.saveValue(key, user);
			
			log.info("--------------------redis dao save--------------------" + redisService.getValue(key));
			
			/*
			 * save at redis cache 
			 */	
			LoginRes res = new LoginRes(tokenDto, vo.getHost(), vo.getWebsocketPort());
			return DefaultRes.res(StatusCode.OK, ResponseMessage.LOGIN_SUCCESS, res);
		}
		log.info("fail login");
		return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.WRONG_PASSWORD);
		
	}
	//sign up
	//CUD Atomicity  
	@Transactional
	@Override
	public DefaultRes signUp(LoginVO vo) {

		try {
			userDao.insertUser(vo);
			log.info("join " + vo.getUserName());
			return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATED_USER);
		}
		catch (DuplicateKeyException e) { // name 중복
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //Rollback
			return DefaultRes.res(StatusCode.ALREADY_EXSIT_NAME, ResponseMessage.ALREADY_EXIST_NAME);

		} catch (Exception e) { // DB 에러
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //Rollback
			log.error("\n- Exception Detail (below)", e);
			return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
		}

	}
	
}
