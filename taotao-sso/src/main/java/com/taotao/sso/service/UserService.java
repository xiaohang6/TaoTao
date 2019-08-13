package com.taotao.sso.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;

//商品接口
public interface UserService {
	// 检查数据是否存在！
	TaotaoResult checkData(String content, Integer type);

	// 用户注册
	TaotaoResult createUser(TbUser user);

	// 用户登录
	TaotaoResult userLogin(String username, String password, HttpServletRequest request, HttpServletResponse response);

	// token查询
	TaotaoResult getUserByToken(String token);
}
