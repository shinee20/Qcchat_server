package org.qucell.chat.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonUtil {
	 /**
     * Map을 json string으로 변환한다.
     *
     * @param map Map<String, Object>.
     * @return JsonString.
     */
	
	private static final ObjectMapper mapper = new ObjectMapper();
	
	public static String toJsonStr(Object obj) {
		String jsonStr = null;
		
		try {
			jsonStr = mapper.writeValueAsString(obj);
		} catch(JsonProcessingException e) {
			log.error("-----------------start----------------------\n-------Json Processing Exception CATCHED!!!-------");
			throw new RuntimeException(e);
		}
		return jsonStr;
	}
	
	public static String toJsonStrFormatted(Object obj) {
		String jsonStr = null;
		
		try {
			jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
		} catch(JsonProcessingException e) {
			log.error("-----------------start----------------------\n-------Json Processing Exception CATCHED!!!-------");
			throw new RuntimeException(e);
		}
		return jsonStr;
	}
	
	public static <T> T toObj(String str, Class<T> clz) {
		try {
			return mapper.readValue(str, clz);
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
}
