package com.taotao.utils;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 淘淘商城自定义响应结构
 * 
 * 1.首先前端jquery代码也可以实现把json字符串转化为jquery对象 (本项目采用在服务器端转化！)
 * 
 * 2.在服务器端转化成了对象以后再返回给前端！
 * 
 * 3.转对象与resposeboby区别：但是加上 @ResponseBody 后返回结果不会被解析为跳转路径， 而是直接写入 HTTP response
 * body 中。 比如异步获取 json 数据，加上 @ResponseBody 后， 会直接返回 json 数据。@RequestBody 将 HTTP
 * 请求正文插入方法中， 使用适合的 HttpMessageConverter 将请求体写入某个对象
 */

// 这个工具类的作用是：将json字符串转化为其他
public class JsonUtils {

	// 定义jackson对象
	private static final ObjectMapper MAPPER = new ObjectMapper();

	/**
	 * 将对象转换成json字符串。
	 * <p>
	 * Title: pojoToJson
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param data
	 * @return
	 */
	public static String objectToJson(Object data) {
		try {
			String string = MAPPER.writeValueAsString(data);
			return string;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将json结果集转化为对象
	 * 
	 * @param jsonData
	 *            json数据
	 * @param clazz
	 *            对象中的object类型
	 * @return
	 */
	public static <T> T jsonToPojo(String jsonData, Class<T> beanType) {
		try {
			T t = MAPPER.readValue(jsonData, beanType);
			return t;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将json数据转换成pojo对象list
	 * <p>
	 * Title: jsonToList
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param jsonData
	 * @param beanType
	 * @return
	 */
	public static <T> List<T> jsonToList(String jsonData, Class<T> beanType) {
		JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, beanType);
		try {
			List<T> list = MAPPER.readValue(jsonData, javaType);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
