package org.qucell.chat.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.qucell.chat.service.JwtService;
import org.qucell.chat.util.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtServiceImpl implements JwtService {

	private static final String SALT="qcSecret";
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginServiceImpl.class);

	@Override
	public <T> String create(String key, T data, String subject) {
		// TODO Auto-generated method stub
		//create jwt session

		String jwt = Jwts.builder()
				.setHeaderParam("typ", "JWT")
				.setHeaderParam("regDate", System.currentTimeMillis())
				.setSubject(subject)
				.setExpiration(new Date(System.currentTimeMillis()+86400*1000*2))
				.claim(key, data)
				.signWith(SignatureAlgorithm.HS256, this.generateKey())
				.compact();
		return jwt;
	}

	private byte[] generateKey(){
		byte[] key = null;
		try {
			key = SALT.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			if(LOGGER.isInfoEnabled()){
				e.printStackTrace();
			}else{
				LOGGER.error("Making JWT Key Error ::: {}", e.getMessage());
			}
		}

		return key;
	}


	@Override
	public Map<String, Object> get(String key) {
		// TODO Auto-generated method stub

		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		String jwt = request.getHeader("Authorization").split(" ")[1];
		Jws<Claims> claims = null;
		//json web signature

		try {
			claims = Jwts.parser()
					.setSigningKey(SALT.getBytes("UTF-8"))
					.parseClaimsJws(jwt);
		}
		catch(Exception e) {

			if(LOGGER.isInfoEnabled()){
				e.printStackTrace();
			}else{
				LOGGER.error(e.getMessage());
			}
			throw new UnauthorizedException();
		}
		
		@SuppressWarnings("unchecked")
		Map<String, Object> value = (LinkedHashMap<String, Object>)claims.getBody().get(key);
		
		System.out.println(value);
		return value;
	}

	@Override
	public int getUserId() {
		// TODO Auto-generated method stub
		
		Integer userId = (Integer)this.get("user").get("id");
		return (int)userId;
	}

	@Override
	public boolean isUsable(String jwt) {
		// TODO Auto-generated method stub
		
		try {
			Jws<Claims> claims = Jwts.parser()
					.setSigningKey(this.generateKey())
					.parseClaimsJws(jwt);
			return true;
		}
		catch(Exception e) {
			//catch all exception 
			if(LOGGER.isInfoEnabled()){
				e.printStackTrace();
			}else{
				LOGGER.error(e.getMessage());
			}
			throw new UnauthorizedException();
		}
	
	}
	/*
	 * claim으로 변환도중 예외가 발생하면 유효하지 않은 토큰으로 판단하고, 예외를 핸들링 해줍니다.

		변환도중 발생하는 에러는 아래의 5가지입니다. 

	 * 1) ExpiredJwtException : JWT를 생성할 때 지정한 유효기간 초과할 때.

		2) UnsupportedJwtException : 예상하는 형식과 일치하지 않는 특정 형식이나 구성의 JWT일 때
		
		3) MalformedJwtException : JWT가 올바르게 구성되지 않았을 때
		
		4) SignatureException :  JWT의 기존 서명을 확인하지 못했을 때
		
		5) IllegalArgumentException


	 */

}
