package com.taotao.sso.dao;

//因为两种 集群和单机 的方式不同
//所以为了适应两种方式
//对于两种方式的不同分别实现了两种不同的实现类
public interface JedisClient {

	String get(String key);

	String set(String key, String value);

	String hget(String hkey, String key);

	// hash
	long hset(String hkey, String key, String value);

	// 自增(加一！！)
	long incr(String key);

	// 设置有效期
	long expire(String key, int second);

	// 可以来看还有多长时间过期
	long ttl(String key);

	long del(String key);

	long hdel(String hkey, String key);

}
