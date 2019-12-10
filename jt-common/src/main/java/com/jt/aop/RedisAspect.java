package com.jt.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.jt.anno.Cache;
import com.jt.util.ObjectMapperUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

@Component // 交给spring容器管理
@Aspect // 标识该类是一个切面类
public class RedisAspect {

	@Autowired(required=false)
	private JedisCluster jedis;

	@Around("@annotation(cache)") // 注入了该参数的值
	public Object around(ProceedingJoinPoint point, Cache cache) {
		// 先去缓存中找
		String key = getKey(point, cache);
		String result = jedis.get(key);
		System.out.println("从缓冲中取"+result);
		Object data = null;
		try {
			if (StringUtils.isEmpty(result)) {// 缓存中没有
				// 查询数据库
				data = point.proceed();
				// 将从数据库查询到的结果放入redis
				String json = ObjectMapperUtil.toString(data);
				// 这里先不设置缓存超时时间,默认永不超时
				jedis.set(key, json);
				System.out.println("缓存中没有数据,查询了数据库");
			} else {// 缓存中有,直接返回
					// 需要先获取业务方法返回值类型,确定将从redis中取的字符串转为对应的对象类型
				Class<?> classType = getClassType(point);
				
				data = ObjectMapperUtil.toObject(result, classType);
				System.out.println("缓存中有,直接返回");
			}
		} catch (Throwable e) {
			System.out.println("走了切面异常");
			e.printStackTrace();
			return new RuntimeException();// 包装为运行时异常
		}
		return data;

		// 获取key
//		String key = getKey(point,cache);
//		// 先从缓存中获取数据
//		String result = jedis.get(key);
//		Object data = null;// 业务层返回结果
//		try {
//			// 判断缓存中是否有数据
//			if (StringUtils.isEmpty(result)) {// 缓存中没有数据
//				// 去数据库查询
//				data = point.proceed();
//				// 将查询结果放入缓存(将对象转为字符串)
//				String json = ObjectMapperUtil.toString(data);
//				// 在放入缓存时应设置超时时间,如果用户设置了就用用户的,没有定义就设置永不超时
//
//				jedis.set(key, json);
//				System.out.println("数据库没有数据,查询了数据库");
//			} else {// 缓存中有数据
//					// 考虑到需要将字符串转换为对应的对象类型,这里需要获取方法的返回值类型
//				Class<?> targetClass = getClassType(point);
//				data = ObjectMapperUtil.toObject(result, targetClass);
//				System.out.println("缓存中有数据,直接返回");
//			}
//
//		} catch (Throwable e) {
//			e.printStackTrace();
//			throw new RuntimeException();
//		}
//		// 返回结果
//		return data;
	}

	/**
	 * 获取redis键值
	 * 
	 * @param point
	 * @param cache
	 * @return
	 */
	@Around("@annotation(cache)")
	public String getKey(ProceedingJoinPoint point, Cache cache) {
		// 如果用户在cache注解中设置了缓存的键值,就使用该键值,如果注解的内容为空,拼接一个方法名+第一个参数值
		String key = cache.key();
		String param = String.valueOf(point.getArgs()[0]);
		MethodSignature signature = (MethodSignature) point.getSignature();// 获取方法签名信息,这里强转为子类对象,因为子类对象可以获取方法名
		String methodName = signature.getName();// 获取方法名
		signature.getExceptionTypes();// 获取异常类型数组
		signature.getParameterTypes();// 获取参数类型数组
		signature.getParameterNames();// 获取参数名数组
		if (!StringUtils.isEmpty(key)) {
			key = key + "_" + param;
		} else {
			key = methodName + "_" + param;
		}
		return key;
	}

	/**
	 * 
	 * 获取业务方法执行的返回值类型
	 * 
	 * @param point
	 * @return
	 */
	public Class<?> getClassType(ProceedingJoinPoint point) {
		MethodSignature signature = (MethodSignature) point.getSignature();
		Class<?> returnType = signature.getReturnType();// 获取方法返回值类型,用于确定将从缓存中获取的对象转换为对应的对象类型
		System.out.println(returnType);
		return returnType;
	}
}
