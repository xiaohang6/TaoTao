package com.taotao.sso.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.pojo.TbUserExample.Criteria;
import com.taotao.sso.dao.JedisClient;
import com.taotao.sso.service.UserService;
import com.taotao.utils.CookieUtils;
import com.taotao.utils.JsonUtils;

/**
 * 用户管理service
 * 
 * @author 12742
 *
 */
@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private TbUserMapper tbUserMapper;

	// 注入reids的实现类
	@Autowired
	private JedisClient jedisClient;

	@Value("${REDIS_USER_SESSION_KEY}")
	private String REDIS_USER_SESSION_KEY;
	// 过期时间(按照秒来算)
	@Value("${SSO_SESSION_EXPIRE}")
	private Integer SSO_SESSION_EXPIRE;

	// 检查数据是否已经存在
	@Override
	public TaotaoResult checkData(String content, Integer type) {
		// 对数据类型进行检查
		// 创建查询条件
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		// 对数据进行校验：1、2、3分别代表username、phone、email
		// 用户名校验

		if (type == 1) {
			criteria.andUsernameEqualTo(content);
		} else if (type == 2) {
			criteria.andPhoneEqualTo(content);
		} else if (type == 3) {
			criteria.andEmailEqualTo(content);
		}
		// 执行查询
		List<TbUser> list = tbUserMapper.selectByExample(example);
		if (list == null || list.size() == 0) {
			return TaotaoResult.ok(true);
		}
		return TaotaoResult.ok(false);
	}

	// 注册
	@Override
	public TaotaoResult createUser(TbUser user) {
		Date date = new Date();
		user.setCreated(date);
		user.setUpdated(date);
		// 密码加密（spring中自带md5加密）
		// md5加密
		user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
		tbUserMapper.insert(user);
		return TaotaoResult.ok();
	}

	// 登录
	@Override
	public TaotaoResult userLogin(String username, String password, HttpServletRequest request,
			HttpServletResponse response) {

		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		List<TbUser> list = tbUserMapper.selectByExample(example);
		System.out.println(list.get(0).getUsername());
		if (list == null || list.size() == 0) {
			return TaotaoResult.build(400, "用户名或密码错误！");
		}
		TbUser user = list.get(0);
		// 验证密码正确
		if (!DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword())) {
			return TaotaoResult.build(400, "用户名或密码错误！");
		}
		// 生成token（sessionId）
		String token = UUID.randomUUID().toString();
		// 为了安全把密码清了
		user.setPassword("");
		// 把用户信息写入redis
		// token 长得很乱 所以想要分一下类所以用hset
		jedisClient.set(REDIS_USER_SESSION_KEY + ":" + token, JsonUtils.objectToJson(user));
		// 设置session的过期时间
		jedisClient.expire(REDIS_USER_SESSION_KEY + ":" + token, SSO_SESSION_EXPIRE);
		// 添加写cookies的逻辑(使用cookie默认的时间关闭浏览器失效)
		CookieUtils.setCookie(request, response, "TT_TOKEN", token);
		// 返回token
		return TaotaoResult.ok(token);
	}

	// 判断是否过期
	@Override
	public TaotaoResult getUserByToken(String token) {
		// 根据token从redis中查询用户信息
		String json = jedisClient.get(REDIS_USER_SESSION_KEY + ":" + token);
		// 判断json是否为空
		if (json == null) {
			return TaotaoResult.build(400, "session已经过期，请重新登录！");
		}
		// 更新过期时间
		jedisClient.expire(REDIS_USER_SESSION_KEY + ":" + token, SSO_SESSION_EXPIRE);
		// 返回用户信息
		return TaotaoResult.ok(JsonUtils.jsonToPojo(json, TbUser.class));
	}

}
