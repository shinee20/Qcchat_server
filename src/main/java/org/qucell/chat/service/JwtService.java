package org.qucell.chat.service;

import static com.auth0.jwt.JWT.require;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class JwtService {
	
	@Value("${JWT.SALT}")
	private String SALT;
	
	@Value("${JWT.ISSUER}")
	private String ISSUER;
	
	/**
	 * create token 
	 * @param users id (generate automatically at login)
	 * @return token
	 */
	public String create(final int userId) {
		try {
            JWTCreator.Builder b = JWT.create();
            b.withIssuer(ISSUER);
            b.withClaim("user_id", userId);
            b.withExpiresAt(new Date(System.currentTimeMillis()+86400*1000*2));
            //token available one day
            return b.sign(Algorithm.HMAC256(SALT));
        } catch (JWTCreationException JwtCreationException) {
        	log.info(JwtCreationException.getMessage());
        }
        return null;
	}

	/**
     * 토큰 해독
     *
     * @param token 토큰
     * @return 로그인한 사용자의 회원 고유 ID
     */
    public Token decode(final String token) {
        try {
            final JWTVerifier jwtVerifier = require(Algorithm.HMAC256(SALT)).withIssuer(ISSUER).build();
            DecodedJWT decodedJWT = jwtVerifier.verify(token);
            return new Token(decodedJWT.getClaim("user_id").asLong().intValue());
        } catch (JWTVerificationException jve) {
        	log.error("\n- Exception Detail (below)", jve);
        } catch (Exception e) {
        	log.error("\n- Exception Detail (below)", e);
        }
        return new Token();
    }
    
    public static class Token {
        //토큰에 담길 정보 필드
        //초기값을 -1로 설정함으로써 로그인 실패시 -1반환
        private int user_id = -1;

        public Token() {
        }

        public Token(final int user_id) {
            this.user_id = user_id;
        }

        public int getUser_id() {
            return user_id;
        }
    }

    //반환될 토큰Res
    public static class TokenRes {
        //실제 토큰
        private String token;

        public TokenRes() {
        }

        public TokenRes(final String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

		@Override
		public String toString() {
			return "TokenRes [token=" + token + "]";
		}
        
    }

}
