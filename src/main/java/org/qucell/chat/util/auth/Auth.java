package org.qucell.chat.util.auth;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//메소드에 적용
@Target(ElementType.METHOD)

//런타임시까지 참조가능
@Retention(RetentionPolicy.RUNTIME)

//Java Doc에도 표시
@Documented

//상속가능
@Inherited

public @interface Auth {

}
