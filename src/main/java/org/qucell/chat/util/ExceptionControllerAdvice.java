package org.qucell.chat.util;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.qucell.chat.model.DefaultRes;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ExceptionControllerAdvice {
	// DB 사용 관련 예외만 받음
    @ExceptionHandler(value = DataAccessException.class)
    public ResponseEntity returnDBErrorRes(HttpServletRequest req, DataAccessException e) {
        log.error("-----------------start----------------------\n-------DB ExceptionHandler CATCHED!!!-------");
        logEverything(req, e);
        return new ResponseEntity<>(DefaultRes.DB_ERROR_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    // 모든 예외를 받음
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity returnFailDefaultRes(HttpServletRequest req, Exception e) {
        log.error("-----------------start----------------------\n-------Entire ExceptionHandler CATCHED!!!-------");
        logEverything(req, e);
        return new ResponseEntity<>(DefaultRes.FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    // request와 exception에 대한 정보를 모두 로그로 남기는 메소드
    public void logEverything(HttpServletRequest req, Exception e) {
        log.error("- Method : " + req.getMethod());
        log.error("- URI : " + req.getRequestURI());
        log.error("- QueryString : " + req.getQueryString());

        Enumeration<String> stringEnumeration = req.getHeaderNames();
        StringBuffer headerStr = new StringBuffer();
        while(stringEnumeration.hasMoreElements()){
            String keyName = stringEnumeration.nextElement();
            headerStr.append(keyName);
            headerStr.append(" : ");
            headerStr.append(req.getHeader(keyName));
            headerStr.append("\n");
        }
        log.error("- Header (below)\n" + headerStr);

        log.error("- @@@@@ Exception Message @@@@@ ---> : " + e.getMessage());
        log.error("- @@@@@ LOOK UPPER MESSAGE CAREFULLY!!! @@@@@\n\n- Exception Detail (below)", e);

        log.error("-----------------end------------------------\n\n\n");
    }
}
