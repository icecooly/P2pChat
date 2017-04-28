package io.itit.p2p;

import java.util.List;

import com.alibaba.fastjson.JSON;

public class JsonUtil {

	/**
	 *convert object to json string 
	 */
	public static String toJson(Object obj){
		return JSON.toJSONString(obj);
	}
	/**
	 * convert json string to class
	 */
	@SuppressWarnings("unchecked")
	public static <T> T fromJson(String str,Class<?>t){
		return (T) JSON.parseObject(str, t);
	}
	/**
	 *convert json to class list 
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> fromJsonList(String str,Class<?>t){
		return  (List<T>) JSON.parseArray(str, t);	
	}
}
