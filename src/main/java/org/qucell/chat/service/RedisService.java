package org.qucell.chat.service;

import java.util.List;
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
public class RedisService<T> implements InitializingBean{

	//CRUD from redis cache
	//key type = object-type:field
	//value  = object

	private Cache cache;

	@Autowired
	private RedisCacheManager cacheManager;


	@Autowired
	RedisTemplate<String, Object> redisTemplate;

	@Resource(name="redisTemplate")
	private ValueOperations<String, T> valueOps;

	@Resource(name="redisTemplate")
	private SetOperations<String, T> setOps;

	@Resource(name="redisTemplate")
	private ListOperations<String, T> listOps;

	@Resource(name="redisTemplate")
	private ZSetOperations<String, T> zSetOps;

	@Resource(name="redisTemplate")
	private HashOperations<String, String, T> hashOps;

	@Override
	public void afterPropertiesSet() throws Exception {
		

	}

	//R
	public T getValue(String key){
		T data = (T) valueOps.get(key);

		if (data != null) {

			return data;
		}
		return null;
	}
	public List<T> getValueList(String key){
		return listOps.range(key,0,-1);
	}

	//C
	public void saveValue(String key, T t) {
		valueOps.set(key, t);
	}

	public void saveValueList(String key, T t) {
		listOps.rightPush(key, t);
	}
	//U
	public void updateValue(String key, T t)  {
		valueOps.set(key, t);
	}
	//D
	public void deleteValue(String key) {
		valueOps.set(key, null);
	}
	public void deleteValueList(String key) {
		listOps.trim(key, -1, 0);
	}

}
