package org.qucell.chat.service;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

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
	private SetOperations<String, Object> setOps;

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

	public List<Object> getValueList(String key){
		List<Object> obj = (List<Object>) setOps.members(key);

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
	
	//save list at redis
	public void saveValue(String key, List<Object> obj) {
		setOps.add(key, obj);
	
		System.out.println("--------------------redis dao save list--------------------");
		log.info(key + " " + obj.toString());
	}
	
	//update element at redis
	public void updateValue(String key, List<Object> obj, String p[]) {

		setOps.pop(key);
		setOps.add(key, obj);
		
		
		System.out.println("--------------------redis dao update list--------------------");
		log.info(key + " " + obj.toString());
	}
}
