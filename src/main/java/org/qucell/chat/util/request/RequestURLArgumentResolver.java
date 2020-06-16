package org.qucell.chat.util.request;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * updated 20/06/15
 * @author myseo
 */
public class RequestURLArgumentResolver implements HandlerMethodArgumentResolver{

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterAnnotation(RequestURL.class) != null;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		
		HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
		
		return request.getRequestURL().toString();
	}
	
}
