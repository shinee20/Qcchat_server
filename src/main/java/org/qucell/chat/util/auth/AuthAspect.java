package org.qucell.chat.util.auth;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.qucell.chat.model.DefaultRes;
import org.qucell.chat.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Aspect
public class AuthAspect {
	private static final String AUTHORIZATION = "Authorization";

	/**
	 * 실패 시 기본 반환 Response
	 */
	public static final DefaultRes  DEFAULT_RES_401 = DefaultRes.builder().status(401).message("인증 실패: 토큰 없음").build();
	private final static ResponseEntity<DefaultRes> RES_RESPONSE_ENTITY_401 = new ResponseEntity<>(DEFAULT_RES_401, HttpStatus.UNAUTHORIZED);

	public final static DefaultRes DEFAULT_RES_403 = DefaultRes.builder().status(403).message("인가 실패: 유효하지 않은 토큰").build();
    private final static ResponseEntity<DefaultRes> RES_RESPONSE_ENTITY_403 = new ResponseEntity<>(DEFAULT_RES_403, HttpStatus.FORBIDDEN);

    /**
     * Repository 의존성 주입
     */
    private final HttpServletRequest httpServletRequest;
    private final JwtService jwtService;

    public AuthAspect(final HttpServletRequest httpServletRequest, final JwtService jwtService) {
        this.httpServletRequest = httpServletRequest;
        this.jwtService = jwtService;
    }
    
    /**
     * 토큰에서 userId 추출해서 반환
     * @param pjp
     * @return
     * @throws Throwable
     */
    //항상 @annotation 패키지 이름을 실제 사용할 annotation 경로로 맞춰줘야 한다.
    @Around("@annotation(org.qucell.chat.util.auth.Auth)")
    public Object around(final ProceedingJoinPoint pjp) throws Throwable {

        final String jwt = httpServletRequest.getHeader(AUTHORIZATION);

        // 토큰 없으면 401 리턴
        if(jwt == null  || jwt == "")
            return RES_RESPONSE_ENTITY_401;

        // 토큰 해독
        final JwtService.Token token = jwtService.decode(jwt);
        int userIdx = token.getUser_id();
        System.out.println("--------------------------%%%%%" + userIdx + "%%%%%%%%%%%%%---------------------");
        // 비정상 토큰인 경우 403 리턴
        if(userIdx < 1)
            return RES_RESPONSE_ENTITY_403;

        // 컨트롤러의 매개변수인 idx 로 전달
        
        pjp.getArgs()[0] = (Integer)userIdx;
        return pjp.proceed(pjp.getArgs());
    }
    
	
}
