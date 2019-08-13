package com.taotao.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserService;
import com.taotao.utils.ExceptionUtil;

/**
 * 用户controller
 * 
 * @author 12742
 *
 */
@Controller
@RequestMapping("user")
public class UserController {
	@Autowired
	private UserService userService;

	// 检查数据是否存在
	@RequestMapping("/check/{param}/{type}")
	@ResponseBody
	public Object checkData(@PathVariable String param, @PathVariable Integer type, String callback) {

		TaotaoResult result = null;

		// 参数有效性校验
		if (param == null) {
			result = TaotaoResult.build(400, "校验内容不能为空");
		}
		if (type == null) {
			result = TaotaoResult.build(400, "校验内容类型不能为空");
		}
		if (type <= 0 || type > 3) {
			result = TaotaoResult.build(400, "校验内容类型错误");
		}
		// 校验出错
		if (null != result) {
			if (null != callback) {
				MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
				mappingJacksonValue.setJsonpFunction(callback);
				return mappingJacksonValue;
			} else {
				return result;
			}
		}
		// 调用服务
		try {
			result = userService.checkData(param, type);

		} catch (Exception e) {
			result = TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}

		if (null != callback) {
			MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
			mappingJacksonValue.setJsonpFunction(callback);
			return mappingJacksonValue;
		} else {
			return result;
		}
	}

	// 注册用户（新建用户）
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	@ResponseBody
	public TaotaoResult createUser(TbUser user) {
		TaotaoResult result = null;
		try {
			result = userService.createUser(user);
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}

		return result;
	}

	// 用户登录
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public TaotaoResult userLogin(String username, String password, HttpServletRequest request,
			HttpServletResponse response) {
		TaotaoResult result = null;
		try {
			result = userService.userLogin(username, password, request, response);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}

	}

	/**
	 * 根据token判断用户是否登录或者session是否过期。
	 * 
	 * 接收token，根据token到redis中取用户信息。判断token字符串是否对应用户信息，
	 * 
	 * 如果不对应说明token非法或者session已过期。取到了说明用户就是正常的登录状态。
	 * 
	 * 返回用户信息，同时重置用户的过期时间。
	 */
	@RequestMapping("/token/{token}")
	@ResponseBody
	public Object getUserByToken(@PathVariable String token, String callback) {
		TaotaoResult result = null;
		try {
			result = userService.getUserByToken(token);
		} catch (Exception e) {
			e.printStackTrace();
			result = TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}

		// 判断是否为jsonp调用
		if (callback == null) {
			return result;
		} else {
			MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
			mappingJacksonValue.setJsonpFunction(callback);
			return mappingJacksonValue;
		}

	}

}
