package org.qucell.chat.service;

import java.io.IOException;

import javax.annotation.Resource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RedisService implements InitializingBean{
	
	//CRUD from redis cache
	//key type = object-type:field
	//value  = object
	
	private Cache cache;
	
	@Autowired
	private RedisCacheManager cacheManager;
	

	@Autowired
	RedisTemplate<String, Object> redisTemplate;
	
	@Resource(name="redisTemplate")
	private ValueOperations<String, Object> valueOps;

	@Resource(name="redisTemplate")
	private SetOperations<String, String> setOps;

	@Resource(name="redisTemplate")
	private ListOperations<String, Object> listOps;

	@Resource(name="redisTemplate")
	private ZSetOperations<String, String> zSetOps;

	@Resource(name="redisTemplate")
	private HashOperations<String, String, Object> hashOps;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		this.cache = cacheManager.getCache("user");
		
	}

	//object -> json string
//	public String objectToString(Object obj) throws JsonProcessingException {
//		ObjectMapper objectMapper = new ObjectMapper();
//		
//		String jsonString = objectMapper.writeValueAsString(obj);
//		
//		return jsonString;
//	}
//	
//	public Object stringToObject(String value) throws JsonParseException, JsonMappingException, IOException {
//		ObjectMapper objectMapper = new ObjectMapper();
//		
//		Object obj = objectMapper.readValue(value, Object.class);
//		return obj;
//	}
	//R
	public Object getValue(String key){
		Object obj = (Object) valueOps.get(key);
		
		if (obj != null) {
			
			System.out.println("--------------------redis dao get--------------------");
			log.info(key + " " + obj.toString());
			
			return obj;
		}
		return null;
	}
	
	//C
	public void saveValue(String key, Object obj) {
		
		valueOps.set(key, obj);
		System.out.println("--------------------redis dao save--------------------");
		log.info(key + " " + obj.toString());
	}
	
	//U
	public void updateValue(String key, Object obj)  {
		valueOps.set(key, obj);
	}
	
	//D
	public void deleteValue(String key) {
		valueOps.set(key, null);
	}
}
