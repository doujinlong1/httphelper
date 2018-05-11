package com.rrc.finance.httphelper.utils;

import com.alibaba.fastjson.JSON;
/**
 * json工具类，使用了fastjson
 * @author doujinlong
 *
 */
public class JsonUtil {

	
	public static String toJson(Object object){
		if (object == null) {
			return "{}";
		}
		return JSON.toJSONString(object);
	}
	
	public static <T> T parseFromJson(String str,Class<T> clazz){
		if (str == null || str.isEmpty()) {
			return null;
		}
		return JSON.parseObject(str, clazz);
		
	}
	
	
}
