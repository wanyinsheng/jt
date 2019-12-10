package com.jt.util;
/**
 * 将对象转换为json串或者将json串转换为对象的工具类
 * @author wan_ys
 *
 */

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperUtil {
	// 创建ObjectMapper对象
	private static final ObjectMapper MAPPER = new ObjectMapper();

	/**
	 * 将对象转换为json串
	 * 
	 * @param target
	 * @return
	 * @throws JsonProcessingException
	 */
	public static String toString(Object target)  {
		String json = null;
		try {
			json = MAPPER.writeValueAsString(target);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("字符串转json异常");
			// 将检查异常转换为运行时异常
			throw new RuntimeException();
		}
		return json;
	}

	/**
	 * 将json串转为指定的类型对象
	 * 
	 * 泛型方法
	 * 
	 * 
	 */
	public static <T> T toObject(String json, Class<T> classTarget) {
		T targe = null;
		try {
			targe = MAPPER.readValue(json, classTarget);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return targe;
	}

}
